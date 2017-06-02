package fr.saagie.smartlogger.simulation

import akka.actor.Cancellable
import fr.saagie.smartlogger.io.input.{InputManager, LogBatch, LogParser}
import fr.saagie.smartlogger.io.output.Alerter
import fr.saagie.smartlogger.io.output.notifier.MailNotifier
import fr.saagie.smartlogger.ml.SmartAnalyzer
import org.apache.spark.ml.classification.NaiveBayes

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Sorting

/**
  * This is a stub of the smartlogger main class, used for the integration of Ml, input and output modules
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
class StubMainIntegrationML_I_O {

  var resultt : Seq[(String, Double)] = Seq.empty
  var sched : Cancellable = _
  var input : InputManager = _

  /**
    * @TODO Add comments
    */
  def closeInput() : Unit = input.close()

  /**
    * @TODO Add comments
    */
  def stopSchedule() : Unit = {
    while (!sched.isCancelled) sched.cancel()
  }

  /**
    * @TODO Add comments
    */
  def getResult : Seq[(String, Double)] = resultt

  /**
    * @TODO Add comments
    */
  def main(args: Array[String]): Unit = {
    val smartAnalyzer = new SmartAnalyzer(new NaiveBayes)

    // Open file and retrieve data.
    val source = Source.fromFile("src/test/resources/TrainData.txt")
    val data = source.mkString
    source.close()

    // Train Machine Learning.
    val trainSeq = LogParser.parseTrainingData(data)
    smartAnalyzer.train(trainSeq)

    // Open server.
    input = new InputManager
    input.open()

    // Configuring the outputs
    val alerter = new Alerter()
    var recipient: Seq[String] = Seq.empty
    recipient = recipient.+:("franck.caron76@gmail.com")
    recipient = recipient.+:("nic.gille@gmail.com")
    recipient = recipient.+:("gregoire.pommier@etu.univ-rouen.fr")

    val notifier = new MailNotifier("Alerte !")
    notifier.setRecipients(recipient)
    alerter.addNotifier(notifier)

    // Execute on each X seconds the same part of code.
    val system = akka.actor.ActorSystem("smartlogger")

    sched = system.scheduler.schedule(0 seconds, 10 seconds) {

      // Getting batch and using it to predict
      val batch = LogBatch.getBatch()
      var result = smartAnalyzer.predict(batch)
      resultt = resultt.union(result)

      // Sorting to put the biggest critically at firsts positions
      result = Sorting.stableSort(result,
        (e1: (String, Double), e2: (String, Double))
        => e1._2 > e2._2)

      // Getting into a message all the critically above the
      // limit chosen beforehand
      var counter = 0
      var message = new String
      while (counter < result.size && result(counter)._2 >= 1.0) {
        message = message ++ result(counter)._1 ++ "\n"
        counter += 1
      }

      // If there is at least one critical log, alert all notifiers.
      if (counter > 0) {
        alerter.notifyAll(message)
      }
    }
  }
}
