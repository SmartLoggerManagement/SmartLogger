package fr.saagie.smartlogger.ml

import java.rmi.NotBoundException

import org.apache.hadoop.conf.Configuration
import org.apache.spark.ml.classification._
import org.apache.spark.ml.feature._
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.{Row, SparkSession}

/**
  * Standard implementation of an analyzer.
  *
  * @author Jordan Baudin
  * @since SmartLogger 0.1
  * @version 1.0
  */
class SmartAnalyzer[Vector,
E <: ProbabilisticClassifier[Vector, E, M],
M <: ProbabilisticClassificationModel[Vector, M]]
(var algo: Classifier[Vector, E, M]) extends IAnalyzer {
  var model: PipelineModel = _



  /**
    * @inheritdoc
    */
  def train(data: Seq[(String, Double)]): Unit = {
    if (data == null) {
      throw new IllegalArgumentException("data shouldn't be null")
    }

    // Creating the DataFrame of the data
    val spark = SparkSession
      .builder
      .master("local")
      .appName("SmartLogger")
      .getOrCreate()


    val logs = buildingDataFrameWithLabels(data)

    val document = spark.createDataFrame(logs).toDF

    document show

    // Creating the Pipeline doing the conversion

    val tokenizer = new Tokenizer()
      .setInputCol("message")
      .setOutputCol("words")
    val hashingTF = new HashingTF()
      .setNumFeatures(1000)
      .setInputCol(tokenizer.getOutputCol)
      .setOutputCol("wordsFeatures")

    val formula = new RFormula()
      .setFormula("criticality ~ errorLevel + date + hour + threadId + filename + line + " + hashingTF.getOutputCol)
      .setFeaturesCol("features")
      .setLabelCol("label")

    val pipeline = new Pipeline()
      .setStages(Array.apply(tokenizer, hashingTF, formula, algo))

    // Train the model

    model = pipeline.fit(document)

    spark.close()

  }

  /**
    * @inheritdoc
    */
  def predict(data: Seq[String]): Seq[(String, Double)] = {
    if (model == null) {
      throw new NotBoundException("The fr.saagie.smartlogger.ml.SmartAnalyzer didn't train before")
    }
    if (data == null) {
      throw new IllegalArgumentException("data shouldn't be null")
    }

    // Creating the DataFrame of the data
    val spark = SparkSession
      .builder
      .master("local")
      .appName("SmartLogger")
      .getOrCreate()


    val logs = buildingDataFrame(data)

    val document = spark.createDataFrame(logs)
      .toDF

    document.show


    // Making the predictions according to the model and the DataFrame
    val result1 = model.transform(document)
      .select("message", "prediction")
    result1.show
    val result = result1.collect()

    // Seq meant to be returned to inform of the result
    var sequence: Seq[(String, Double)] = Seq.empty

    // Checking every Row for any critic data.
    // Making row by row the sequence.
    result.foreach { case Row(text: String, prediction: Double) =>
      if (prediction >= 2.0) {
        println(text + ":" + prediction)
      }
      sequence = sequence.:+((text, prediction))
    }

    spark.close()

    return sequence

  }

  case class Log(errorLevel: String, date: Int,
                 hour:Long, threadId: Int, filename: String, line: Int, message: String)

  case class LogWithLabel(errorLevel: String, date: Int,
                          hour:Long, threadId: Int, filename: String,
                          line: Int, message: String, criticality: Double)



  // TODO Extract date or hour even if it doesn't match the format.
  private def buildingDataFrameWithLabels(data: Seq[(String, Double)]):Seq[LogWithLabel] = {

    var resultSeq:Seq[LogWithLabel] = Seq.empty

    val logFormat = "([IWEF])([0-1][0-9][0-3][0-9]) *([0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]*) *([0-9]*) *([^:]*):([0-9])*\\] (.*)".r

    var i = 0

    for (line <- data.map(_._1)) {

      line match {
        case logFormat(errorLevel, date, hour, threadId, filename, number, message) =>
          var hourArray = hour.split(":")
          var newHour = hourArray(0).toLong * 60 + hourArray(1).toLong
          resultSeq = resultSeq :+ LogWithLabel(errorLevel, date.toInt, newHour, threadId.toInt, filename, number.toInt, message, data(i)._2)
        case _ =>
          resultSeq = resultSeq :+ LogWithLabel("noErrorLevel", -1, 0, -2, "NoFileName", -3, line, data(i)._2)
      }

      i += 1
    }

    return resultSeq

  }

  private def buildingDataFrame(data:Seq[String]):Seq[Log] = {
    var resultSeq:Seq[Log] = Seq.empty

    val logFormat = "([IWEF])([0-1][0-9][0-3][0-9]) *([0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]*) *([0-9]*) *([^:]*):([0-9])*\\] (.*)".r

    for (line <- data) {

      line match {
        case logFormat(errorLevel, date, hour, threadId, filename, number, message) =>
          var hourArray = hour.split(":")
          var newHour = hourArray(0).toLong * 60 + hourArray(1).toLong
          resultSeq = resultSeq :+ Log(errorLevel, date.toInt, newHour, threadId.toInt, filename, number.toInt, message)
        case _ =>
          resultSeq = resultSeq :+ Log("noErrorLevel", -1, 0, -2, "NoFileName", -3, line)
      }

    }

    return resultSeq

  }
}
