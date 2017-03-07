/**
  * Created by Kero76 on 07/03/17.
  *
  * @author Nicolas GILLE
  * @since SmartLogger 0.2
  * @version 1.0
  */
class AnalyzerDataManager extends DataManager {
  override def sendData(logs: Seq[(Long, String)]): Unit = {

  }

  override def receiveData(logs: Seq[(Long, String)]): Unit = {

  }

  override def checkData(data: String): Boolean = {

  }

  override def addData(data: Seq[(Long, String)]): Unit = {

  }

  def getResults: Seq[(Long, String)] = {
    return Seq.empty
  }

  def getPertinents: Seq[(Long, String)] = {
    return Seq.empty
  }
}
