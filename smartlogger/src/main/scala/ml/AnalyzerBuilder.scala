import org.apache.spark.ml.classification.{DecisionTreeClassifier, LogisticRegression, NaiveBayes, RandomForestClassifier}

import scala.ML.Analyzer

/**
  * Provides an analyzer builder based on the SmartAnalyzer class,
  * and available algorithms in spark ML Library.
  */
object AnalyzerBuilder {
  /**
    * Provides an analyzer based on the NaivesBayes algorithm
    */
  def getNaiveBayes: Analyzer = new SmartAnalyzer(new NaiveBayes())

  /**
    * Provides an analyzer based on the LogisticRegression algorithm
    */
  def getLogisticRegression: Analyzer = new SmartAnalyzer(new LogisticRegression())

  /**
    * Provides an analyzer based on the LogisticRegression algorithm
    */
  def getDecisionTreeClassifier: Analyzer = new SmartAnalyzer(new DecisionTreeClassifier())

  /**
    * Provides an analyzer based on the LogisticRegression algorithm
    */
  def getRandomForestClassifier: Analyzer = new SmartAnalyzer(new RandomForestClassifier())
}
