package fr.saagie.smartlogger

import java.io.{BufferedReader, File, FileReader}
import java.util.UUID

import fr.saagie.smartlogger.db.model.Log
import fr.saagie.smartlogger.db.mysql.MySQLDAOBuilder
import fr.saagie.smartlogger.io.input.{InputManager, LogBatch, LogParser}
import fr.saagie.smartlogger.io.output.Alerter
import fr.saagie.smartlogger.io.output.notifier.{MailNotifier, SlackNotifier}
import fr.saagie.smartlogger.ml.AnalyzerBuilder
import fr.saagie.smartlogger.utils.Properties

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Sorting

/**
  * SmartLogger is the main object of the application start
  * when you run SmartLogger's app.
  *
  * @author Jordan BAUDIN, Nicolas GILLE, Franck CARON
  * @since SmartLogger 0.1
  * @version 1.0
  */
object SmartLogger {
  // ATTRIBUTES
  /**
    * The DAO used for DBMS interaction with SmartLogger
    */
  private val DAO = MySQLDAOBuilder.getLogDAO()

  /**
    * The analyzer used for Analysis and Predicting.
    */
  private val smartAnalyzer = AnalyzerBuilder.getDecisionTreeClassifier

  /**
    * The alerter used by the system
    */
  private val alerter = new Alerter


  // TOOLS
  /**
    * Initializes the data table used for logs' storage.
    * If the table hasn't been built yet, it is created with the initial content
    * which is located inside the file resources/TrainData.txt
    */
  def initializeDAO(): Unit = {
    if (!DAO.exists()) {
      DAO.build()

      // Filling table with initial content
      val reader = new BufferedReader(new FileReader(new File("resources/TrainData.txt")))
      val txt = new StringBuilder
      while (reader.ready()) {
        txt.append(reader.readLine() + '\n')
      }
      reader.close()

      // Parsing content and transforming it into logs.
      val initData: Seq[(String, Double)] = LogParser.parseTrainingData(txt.toString)
      for ((content, label) <- initData) {
        // Creation
        val log = DAO.newInstance()
        log.setContent(content)
        log.setLabel(label)

        // Insertion into Logs' table
        DAO.insert(log)
      }
    }
  }

  /**
    * Defines a new analysis's model by training the system with
    * all data inside the Log's table
    */
  def trainSystem(): Unit = {
    val dbLogs = DAO.get()
    val data: Seq[(String, Double)] = dbLogs.map(log => (log.getContent, log.getLabel))
    smartAnalyzer.train(data)
  }

  /**
    * Defines an alerter which will be used for alerts. Consists at
    * creating its notifiers (Slack, Mail, ...), which will do the alert
    * for a given situation.
    */
  def initializeAlerter(): Unit = {
    // Adding MAIL notifier
    // -- Retrieve infos from properties mail file.
    val mailProps = Properties.MAIL
    val mailRecipients: Seq[String] = mailProps.get("contact").split(",").toSeq
    val mailSubject = mailProps.get("subject")

    // -- Defining notifier
    val mailNotifier = new MailNotifier(mailSubject)
    mailNotifier.setRecipients(mailRecipients)

    // -- Adding it to alerter : Alerts with an email when problems have occurred
    alerter.addNotifier(mailNotifier)


    // Adding SLACK notifier
    // -- Retrieve infos from properties slack file.
    val slackProps = Properties.SLACK
    val slackApiKey = slackProps.get("$apiKey")
    val slackChannel = slackProps.get("thread")
    val slackRecipients: Seq[String] = slackProps.get("contact").split(",").toSeq

    // -- Defining notifier
    val slackNotifier = new SlackNotifier(slackApiKey)
    slackNotifier.setChannel(slackChannel)
    slackNotifier.setRecipients(slackRecipients)

    //  -- Adding it to alerter : Alerts on Slack when problems have occurred
    alerter.addNotifier(slackNotifier)
  }


  // MAIN
  def main(args: Array[String]): Unit = {
    // Initializing DAO
    initializeDAO()
    println("Step 1 completed : The DAO has been initialized")

    // Extracting useful informations to train the system
    trainSystem()
    println("Step 2 completed : The train model has been created")

    // Opening server
    val input = new InputManager()
    input.open()
    println("Step 3 completed : The HTTP request can now be read")

    // Initializing alerter (and its notifiers)
    initializeAlerter()
    println("Step 4 completed : All alerting systems are enabled")

    // Execute on each X seconds the same part of code.
    println("Initialization completed : Waiting for requests...")
    val system = akka.actor.ActorSystem("smartlogger")
    system.scheduler.schedule(0 seconds, 10 seconds) {

      // Getting batch and using it to predict
      val batch  = LogBatch.getBatch()
      var result = smartAnalyzer.predict(batch)

      // Loop on each logs received and insert logs on Database.
      for ((content, label) <- result) {
        val log = new Log(DAO.getAttributeFactory())
        log.setId(UUID.randomUUID())
        log.setContent(content)
        log.setLabel(label)

        DAO.insert(log)
      }

      // Sorting to put the biggest critically at firsts positions
      result = Sorting.stableSort(result,
      (e1: (String, Double), e2: (String, Double))
      => e1._2 > e2._2)

      // Getting into a message all the critically above the
      // limit chosen beforehand
      var counter = 0
      var message = new String
      while (counter < result.size && result(counter)._2 >= 2.0) {
        message = message ++ result(counter)._1 ++ System.getProperty("line.separator")
        counter += 1
      }

      // If there is at least one critical log, alert all notifiers.
      if (counter > 0) {
        alerter.notifyAll(message)
      }
    }
  }
}
