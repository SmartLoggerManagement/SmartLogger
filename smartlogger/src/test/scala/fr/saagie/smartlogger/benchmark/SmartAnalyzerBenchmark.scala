package fr.saagie.smartlogger.benchmark

import fr.saagie.smartlogger.io.input.LogParser
import fr.saagie.smartlogger.ml.{IAnalyzer, SmartAnalyzer}
import org.apache.spark.ml.classification.{DecisionTreeClassifier, LogisticRegression, NaiveBayes, RandomForestClassifier}
import org.junit.runner.RunWith
import org.scalatest.{FeatureSpec, FunSuite}
import org.scalatest.junit.JUnitRunner

import scala.io.Source

/**This class send a request with a given body to a given uri
  *
  * @author Jordan BAUDIN
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class SmartAnalyzerBenchmark extends FeatureSpec {

  // Function giving the time the thunk take to run
  def time[T](str: String)(thunk: => T): T = {
    val t1 = System.currentTimeMillis
    val x = thunk
    val t2 = System.currentTimeMillis
    println(str + (t2 - t1) + " msecs")
    x
  }

  // Function running the training with example data
  //  & then running the predicting with example data
  //  & testing if the result is good & transmitting
  //  the false results to track.
  def benchmark(analy:IAnalyzer, str:String):Unit = {
    var source = Source.fromFile("src/test/resources/TrainData.txt")
    val data = source.mkString

    source.close()

    val index = data.indexOf('\n', data.length/2)

    val trainData = LogParser.parseTrainingData(data.substring(0, index))
    val predictData = LogParser.parsePredictData(data.substring(index)).drop(2)
    val testingPredictData = LogParser.parseTrainingData(data.substring(index)).drop(1)

    time("The training part of " + str + " took "){analy.train(trainData)}

    val result = time("The predict part of " + str + " took "){analy.predict(predictData)}
    var count = 0.0
    for ((elemResult, elemData) <- result zip testingPredictData) {
      if (elemData._3 != elemResult._3) {
        count += 1
      }
    }

    val nbElem = result.last._1
    val rate = count / nbElem * 100

    println(str + " algorithm failed " + count + " times of "
      + nbElem + " elements making it a failure rate of " + rate + "%")

  }

  scenario("Train & Predict Benchmark Using A 10k Lines File For NaiveBayes") {
    benchmark(new SmartAnalyzer(new NaiveBayes), "NaiveBayes")
  }

  scenario("Train & Predict Benchmark Using A 10k Lines File For DecisionTreeClassifier") {
    benchmark(new SmartAnalyzer(new DecisionTreeClassifier), "DecisionTreeClassifier")
  }

  scenario("Train & Predict Benchmark Using A 10k Lines File For LogisticRegression") {
    benchmark(new SmartAnalyzer(new LogisticRegression), "LogisticRegression")
  }

  scenario("Train & Predict Benchmark Using A 10k Lines File For RandomForestClassifier") {
    benchmark(new SmartAnalyzer(new RandomForestClassifier), "RandomForestClassifier")
  }

}