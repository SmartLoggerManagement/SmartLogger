import java.rmi.NotBoundException

import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification._
import org.apache.spark.ml.feature._
import org.apache.spark.sql.{Row, SparkSession}

import scala.ML.IAnalyzer

/**
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
  override def train(data: Seq[(Long, String, Double)]): Unit = {
    if (data == null) {
      throw new IllegalArgumentException("data shouldn't be null")
    }
    // Creating the DataFrame of the data
    val spark = SparkSession
      .builder
      .master("local")
      .appName("SmartLogger")
      .getOrCreate()

    val document = spark.createDataFrame(data)
      .toDF("id", "text", "label")

    // Creating the Pipeline doing the conversion
    val tokenizer = new Tokenizer()
      .setInputCol("text")
      .setOutputCol("words")
    val hashingTF = new HashingTF()
      .setNumFeatures(1000)
      .setInputCol(tokenizer.getOutputCol)
      .setOutputCol("features")

    val pipeline = new Pipeline()
      .setStages(Array.apply(tokenizer, hashingTF, algo))

    // Train the model

    model = pipeline.fit(document)

  }

  /**
    * @inheritdoc
    */
  override def predict(data: Seq[(Long, String)]): Seq[(Long, String, Double)] = {
    if (model == null) {
      throw new NotBoundException("The SmartAnalyzer didn't train before")
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
    val document = spark.createDataFrame(data)
      .toDF("id", "text")


    // Making the predictions according to the model and the DataFrame
    val result = model.transform(document)
      .select("id", "text", "prediction")
      .collect()

    // Seq meant to be returned to inform of the result
    var sequence: Seq[(Long, String, Double)] = Seq.empty

    // Checking every Row for any critic data.
    // Making row by row the sequence.
    result.foreach { case Row(id: Long, text: String, prediction: Double) =>
      if (prediction >= 2.0) {
        println(id + ":" + text + ":" + prediction)
      }
      sequence = sequence.:+((id, text, prediction))
    }

    return sequence

  }
}
