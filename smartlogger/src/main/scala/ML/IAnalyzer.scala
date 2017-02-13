package scala.ML

trait IAnalyzer {
  /**
    * @param data Seq describing an ID, a Log, a Label
    */
  def train (data: Seq[(Long, String, Double)]): Unit

  /**
    * @param data Seq describing an ID and a Log
    * @return Seq equals to the input Seq, with a new column which is the prediction
    */
  def predict (data: Seq[(Long, String)]): Seq[(Long, String, Double)]

}
