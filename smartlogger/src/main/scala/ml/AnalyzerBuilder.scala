import org.apache.spark.ml.classification.{DecisionTreeClassifier, LogisticRegression, NaiveBayes, RandomForestClassifier}

import scala.ML.IAnalyzer

/**
  * Provides an analyzer builder based on the SmartAnalyzer class,
  * and available algorithms in spark ML Library.
  *
  * @author Jordan Baudin
  * @since SmartLogger 0.1
  * @version 1.0
  */
object AnalyzerBuilder {
  /**
    * Provides an analyzer based on the NaivesBayes algorithm.
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def getNaiveBayes: IAnalyzer = new SmartIAnalyzer(new NaiveBayes())

  /**
    * Provides an analyzer based on the LogisticRegression algorithm.
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def getLogisticRegression: IAnalyzer = new SmartIAnalyzer(new LogisticRegression())

  /**
    * Provides an analyzer based on the LogisticRegression algorithm.
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def getDecisionTreeClassifier: IAnalyzer = new SmartIAnalyzer(new DecisionTreeClassifier())

  /**
    * Provides an analyzer based on the LogisticRegression algorithm.
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def getRandomForestClassifier: IAnalyzer = new SmartIAnalyzer(new RandomForestClassifier())
}
