package scala.ML

trait IAnalyzer {

  def train (data: Seq[String]): Unit

  def predict (data: Seq[String]): Unit

  def getWeights(): Seq[Any]

  def setWeights(data: Seq[Any]): Unit
}