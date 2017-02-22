import input.LogParser
import org.apache.spark.ml.classification.NaiveBayes
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.io.Source

/**
  * Created by Jordan Baudin on 22/02/17.
  */
@RunWith(classOf[JUnitRunner])
class SmartAnalyzerBenchmark extends FunSuite {

  def time[T](str: String)(thunk: => T): T = {
    val t1 = System.currentTimeMillis
    val x = thunk
    val t2 = System.currentTimeMillis
    println(str + (t2 - t1) + " msecs")
    x
  }

  test("Train & Predict Benchmark Using A 10k Lines File") {
    var source = Source.fromFile("src/test/scala/IAnalysor/TrainData.txt")
    val trainData = LogParser.parseTrainingData(source.mkString)
    source.close()

    val analy = new SmartAnalyzer(new NaiveBayes)

    time("The training part took "){analy.train(trainData)}

    source = Source.fromFile("src/test/scala/IAnalysor/TrainData.txt")
    val predictData = LogParser.parsePredictData(source.mkString)

    time("The predict part took "){analy.predict(predictData)}
  }


}
