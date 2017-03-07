import input.{InputManager, LogBatch, LogParser}
import org.apache.spark.ml.classification.NaiveBayes

import scala.concurrent.duration._
import scala.io.Source

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
    InputManager.open()

    // Execute on each X seconds the same part of code.
    val system = akka.actor.ActorSystem("smartlogger")
    system.scheduler.schedule(0 seconds, 10 seconds) {
      val batch = LogBatch.getBatch()
      smartAnalyzer.predict(batch)
    }
  }
}
