package fr.saagie.smartlogger

import java.util.UUID

import fr.saagie.smartlogger.db.model.Log
import fr.saagie.smartlogger.db.mysql.{AttrMySQLFactory, LogDAO}
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
  * @author Jordan BAUDIN, Nicolas GILLE
  * @since SmartLogger 0.1
  * @version 1.0
  */
object SmartLogger {
  // MAIN
  def main(args: Array[String]): Unit = {
    // Build table if necessary.
    LogDAO.build()

    // Instantiate algorithm used by SmartLogger
    val smartAnalyzer = AnalyzerBuilder.getNaiveBayes

    // Get logs stored in Database.
    val dbLogs = LogDAO.get()

    // Concat all logs into a string where each logs were separated by "line.separator"
    val data = new String
    for (dbLog <- dbLogs) {
      data.concat(dbLog.getContent)
      data.concat(System.getProperty("line.separator"))
    }

    // Train Machine Learning.
    val trainSeq = LogParser.parseTrainingData(data)
    smartAnalyzer.train(trainSeq)

    // Open server.
    val input = new InputManager()
    input.open()

    // Retrieve contacts from properties mail file and add it on sequence of recipients.
    var recipient: Seq[String] = Seq.empty
    val emailProperties = Properties.MAIL
    var contactSplit = emailProperties.get("contact").split(",")
    for (c <- contactSplit) {
      recipient = recipient.+:(c)
    }

    // Prepare email subject and set recipients
    val emailSubject = Properties.MAIL.get("subject")
    val emailNotifier = new MailNotifier(emailSubject)
    emailNotifier.setRecipients(recipient)

    // Add mail notifier on alerter to send email when problems occurred.
    val alerter = new Alerter()
    alerter.addNotifier(emailNotifier)

    // Initialize Slack properties.
    val slackProperties = Properties.SLACK
    val apiKey = slackProperties.get("$apiKey")
    val slackSender = new SlackNotifier(apiKey)

    // Retrieve channel and contacts
    val channel = slackProperties.get("thread")
    slackSender setChannel channel

    recipient = Seq.empty
    contactSplit = slackProperties.get("contact").split(",")
    for (c <- contactSplit) {
      recipient = recipient.+:(c)
    }
    slackSender setRecipients recipient

    // Add slack notifier on alerter to send message on Slack when problems occurred
    alerter.addNotifier(slackSender)

    // Execute on each X seconds the same part of code.
    val system = akka.actor.ActorSystem("smartlogger")

    system.scheduler.schedule(0 seconds, 10 seconds) {

      // Getting batch and using it to predict
      val batch  = LogBatch.getBatch()
      var result = smartAnalyzer.predict(batch)

      // Loop on each logs received and insert logs on Database.
      for (r <- result) {
        val log = new Log(AttrMySQLFactory)
        log.setId(UUID.randomUUID())
        log.setContent(r._2)
        LogDAO.insert(log)
      }

      // Sorting to put the biggest critically at firsts positions
      result = Sorting.stableSort(result,
      (e1: (Long, String, Double), e2: (Long, String, Double))
      => e1._3 > e2._3)

      // Getting into a message all the critically above the
      // limit chosen beforehand
      var counter = 0
      var message = new String
      while (counter < result.size && result(counter)._3 >= 2.0) {
        message = message ++ result(counter)._2 ++ System.getProperty("line.separator")
        counter += 1
      }

      // If there is at least one critical log, alert all notifiers.
      if (counter > 0) {
        alerter.notifyAll(message)
      }
    }
  }
}
