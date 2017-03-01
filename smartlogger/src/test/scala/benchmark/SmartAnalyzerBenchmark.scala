import input.LogParser
import org.apache.spark.ml.classification.{DecisionTreeClassifier, LogisticRegression, NaiveBayes, RandomForestClassifier}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.ML.IAnalyzer
import scala.io.Source

/**
  * Created by Jordan Baudin on 22/02/17.
  */
@RunWith(classOf[JUnitRunner])
class SmartAnalyzerBenchmark extends FunSuite {

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
  def benchmark(analy:IAnalyzer, str:String):Unit = {
    var source = Source.fromFile("src/test/scala/IAnalysor/TrainData.txt")
    val trainData = LogParser.parseTrainingData(source.mkString)
    source.close()

    time("The training part of " + str + " took "){analy.train(trainData)}

    source = Source.fromFile("src/test/scala/IAnalysor/TrainData.txt")
    val predictData = LogParser.parsePredictData(source.mkString)

    time("The predict part of " + str + " took "){analy.predict(predictData)}
  }

  test("Train & Predict Benchmark Using A 10k Lines File For NaiveBayes") {
    benchmark(new SmartAnalyzer(new NaiveBayes), "NaiveBayes")
  }

  test("Train & Predict Benchmark Using A 10k Lines File For DecisionTreeClassifier") {
    benchmark(new SmartAnalyzer(new DecisionTreeClassifier), "DecisionTreeClassifier")
  }

  test("Train & Predict Benchmark Using A 10k Lines File For LogisticRegression") {
    benchmark(new SmartAnalyzer(new LogisticRegression), "LogisticRegression")
  }

  test("Train & Predict Benchmark Using A 10k Lines File For RandomForestClassifier") {
    benchmark(new SmartAnalyzer(new RandomForestClassifier), "RandomForestClassifier")
  }

}