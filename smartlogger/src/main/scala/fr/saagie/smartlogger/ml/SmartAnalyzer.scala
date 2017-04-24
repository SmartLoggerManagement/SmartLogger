package fr.saagie.smartlogger.ml

import java.rmi.NotBoundException

import fr.saagie.smartlogger.utils.Properties
import org.apache.spark.SparkContext
import org.apache.spark.ml.classification._
import org.apache.spark.ml.feature._
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.catalyst.expressions.Hour
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
  override def train(data: Seq[(String, Double)]): Unit = {
    if (data == null) {
      throw new IllegalArgumentException("data shouldn't be null")
    }

    // Creating the DataFrame of the data
    val spark = SparkSession
      .builder
      .master("local")
      .appName("SmartLogger")
      .getOrCreate()

    val logs = buildingDataFrame(data.map(_._1))

    val document = spark.createDataFrame(logs zip data.map(_._2))
      .toDF("errorLevel", "date", "hour", "threadId", "filename", "line", "message", "criticality")

    println(document)

    // Creating the Pipeline doing the conversion

    val formula = new RFormula()
      .setFormula("criticality ~ errorLevel + date + hour + threadId + filename + line + message")
      .setFeaturesCol("features")
      .setLabelCol("label")

    val pipeline = new Pipeline()
      .setStages(Array.apply(formula, algo))

    // Train the model

    model = pipeline.fit(document)

  }

  /**
    * @inheritdoc
    */
  override def predict(data: Seq[String]): Seq[(String, Double)] = {
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
      .toDF("errorLevel", "date", "hour", "threadId", "filename", "line", "message")


    // Making the predictions according to the model and the DataFrame
    val result = model.transform(document)
      .select("message", "label")
      .collect()

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

    return sequence

  }

  case class Log(errorLevel: String, date: Int, hour:String, threadId: Int, filename: String, line: Int, message: String)

  private def buildingDataFrame(data:Seq[String]):Seq[Log] = {
    var resultSeq:Seq[Log] = Seq.empty

    val logFormat = Properties.FEATURES.get("logFormat").r

    for (line <- data) {

      line match {
        case logFormat(errorLevel, date, hour, threadId, filename, line, message) =>
          resultSeq = resultSeq :+ Log(errorLevel, date.toInt, hour, threadId.toInt, filename, line.toInt, message)
        case _ =>
          resultSeq = resultSeq :+ Log(null, -1, null, -1, null, -1, line)
      }

    }

    return resultSeq

  }



}
