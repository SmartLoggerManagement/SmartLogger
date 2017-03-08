import input.{InputManager, LogBatch, LogParser}
import org.apache.spark.ml.classification.NaiveBayes
import output.Alerter
import output.mail.MailNotifier

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scala.io.Source
import scala.util.Sorting

/**
  * Created by Nicolas GILLE, Jordan BAUDIN on 07/03/17.
  *
  * @author Jordan BAUDIN, Nicolas GILLE
  * @since SmartLogger 1.0
  * @version 1.0
  */
object SmartLogger {

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
    val input = new InputManager
    input.open()

    // Configuring the outputs
    val alerter = new Alerter()
    alerter.addNotifier(new MailNotifier("Alerte sur la 1.0 de SmartLogger"))

    // Execute on each X seconds the same part of code.
    val system = akka.actor.ActorSystem("smartlogger")

    system.scheduler.schedule(0 seconds, 10 seconds) {

      // Getting batch and using it to predict
      val batch = LogBatch.getBatch()
      val result = smartAnalyzer.predict(batch)

      // Sorting to put the biggest critically at firsts positions
      Sorting.stableSort(result,
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
        alerter.alertAll(message)
      }

    }

  }
}