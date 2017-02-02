package scala.ML

/**
  * Created by teegreg on 31/01/17.
  */
class StubAnalyzer extends IAnalyzer {
  var weights:Seq[Any] = _
  override def train (data: Seq[(Long, String, Double)]): Unit = {

  }

  override def predict (data: Seq[(Long, String)]): Seq[(Long, String, Double)] = {
    return null
  }

  override def getWeights(): String = {
    return null
  }

  override def setWeights(data: Seq[Any]): Unit = {
      weights = data
  }
}
