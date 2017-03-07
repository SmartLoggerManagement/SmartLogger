/**
  * Created by Kero76 on 07/03/17.
  *
  * @author Nicolas GILLE
  * @since SmartLogger 0.2
  * @version 1.0
  */
trait DataManager {

  def sendData(logs: Seq[(Long, String)]): Unit

  def receiveData(logs: Seq[(Long, String)]): Unit

  def checkData(data: String): Boolean

  def addData(data: Seq[(Long, String)]): Unit
}
