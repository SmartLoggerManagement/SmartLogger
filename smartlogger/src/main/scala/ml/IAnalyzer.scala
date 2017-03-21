package scala.ML

/**
  *
  * @author Jordan Baudin
  * @since SmartLogger 0.1
  * @version 1.0
  */
trait IAnalyzer {

  /**
    * Method used to train Machine Learning.
    *
    * @param data
    *   Seq describing an ID, a Log, a Label
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def train(data: Seq[(Long, String, Double)]): Unit

  /**
    * Method used to predict result.
    *
    * You call this method only if you have called previously the method train.
    *
    * @param data
    *   Seq describing an ID and a Log
    * @return
    *   Seq equals to the input Seq, with a new column which is the prediction
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def predict(data: Seq[(Long, String)]): Seq[(Long, String, Double)]

}
