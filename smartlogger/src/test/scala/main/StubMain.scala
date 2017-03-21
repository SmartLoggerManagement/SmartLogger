import akka.actor.Cancellable
import input.{InputManager, LogBatch, LogParser}
import org.apache.spark.ml.classification.NaiveBayes
import output.Alerter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Sorting

/**
  * Created by teegreg on 08/03/17.
  */
class StubMain {

  var resultt : Seq[(Long, String, Double)] = Seq.empty
  var sched : Cancellable = _
  var input : InputManager = _

  def closeInput() : Unit = input.close()

  def stopSchedule() : Unit = {
    while (!sched.isCancelled) sched.cancel()
  }

  def getResult : Seq[(Long, String, Double)] = resultt

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
    input = new InputManager
    input.open()

    // Configuring the outputs
    val alerter = new Alerter()
    alerter.addNotifier(new StubNotifier())

    // Execute on each X seconds the same part of code.
    val system = akka.actor.ActorSystem("smartlogger")

    sched = system.scheduler.schedule(0 seconds, 5 seconds) {

      // Getting batch and using it to predict
      val batch = LogBatch.getBatch()
      var result = smartAnalyzer.predict(batch)
      resultt = resultt.union(result)

      // Sorting to put the biggest critically at firsts positions
      result = Sorting.stableSort(result,
        (e1: (Long, String, Double), e2: (Long, String, Double))
        => e1._3 > e2._3)

      // Getting into a message all the critically above the
      // limit chosen beforehand
      var counter = 0
      var message = new String
      while (counter < result.size && result(counter)._3 >= 1.0) {
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
