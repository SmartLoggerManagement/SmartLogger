package scala.ML

trait IAnalyzer {

    def train (data: Seq[(Long, String, Double)]): Unit

    def predict (data: Seq[(Long, String)]): Seq[(Long, String, Double)]

    def getWeights: String

    def setWeights(data: Seq[Any]): Unit

}
