package scala.ML

/**
  * Created by teegreg on 31/01/17.
  */
class StubAnalyzer extends IAnalyzer {
  var weights:Seq[Any] = _
  override def train (data: Seq[String]): Unit = {

  }

  override def predict (data: Seq[String]): Unit = {

  }

  override def getWeights(): Seq[Any] = {
      weights
  }

  override def setWeights(data: Seq[Any]): Unit = {
      weights = data
  }
}
