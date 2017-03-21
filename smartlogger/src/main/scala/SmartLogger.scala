import input.{IInputManager, LogBatch, LogParser}
import org.apache.spark.ml.classification.NaiveBayes
import output.Alerter
import output.mail.MailNotifier

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scala.io.Source
import scala.util.Sorting

/**
  * SmartLogger is the main object of the application start when you run SmartLogger's app.
  *
  *
  * @author Jordan BAUDIN, Nicolas GILLE
  * @since SmartLogger 0.1
  * @version 1.0
  */
object SmartLogger {

  def main(args: Array[String]): Unit = {
    val smartAnalyzer = new SmartIAnalyzer(new NaiveBayes)

    // Open file and retrieve data.
    val source = Source.fromFile("src/test/resources/TrainData.txt")
    val data = source.mkString
    source.close()

    // Train Machine Learning.
    val trainSeq = LogParser.parseTrainingData(data)
    smartAnalyzer.train(trainSeq)

    // Open server.
    val input = new IInputManager
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

    system.scheduler.schedule(0 seconds, 10 seconds) {

      // Getting batch and using it to predict
      val batch = LogBatch.getBatch()
      var result = smartAnalyzer.predict(batch)

      // Sorting to put the biggest critically at firsts positions
      result = Sorting.stableSort(result,
        (e1: (Long, String, Double), e2: (Long, String, Double))
        => e1._3 > e2._3)

      // Getting into a message all the critically above the
      // limit chosen beforehand
      var counter = 0
      var message = new String
      while (counter < result.size && result(counter)._3 >= 2.0) {
        message = message ++ result(counter)._2 ++ "\n"
        counter += 1
      }

      // If there is at least one critical log, alert all notifiers.
      if (counter > 0) {
        alerter.notifyAll(message)
      }
    }
  }
}