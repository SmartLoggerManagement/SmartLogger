import input.{InputManager, LogBatch}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}

/**
  * Created by teegreg on 03/03/17.
  */
@RunWith(classOf[JUnitRunner])
class ServerTest extends FunSuite with Matchers  {

  test("NormalBehavior") {
    val sv = new InputManager
    sv.open
    val clt : ClientTest = new ClientTest()
    clt.sendRequest("Test Data", "http://127.0.0.1:8080/smartlogger")
    Thread.sleep(2000)
    val batchContent = LogBatch.getBatch()
    batchContent(0)._2 should equal("Test Data")
  }

  test("ChargeTest") {
    //val sv = new Server
    //sv.open
    val clt: ClientTest = new ClientTest()
    for (i <- 0 until 200) {
    clt.sendRequest("Test Data ", "http://127.0.0.1:8080/smartlogger")
    }
    Thread.sleep(2000)
    val batchContent = LogBatch.getBatch()

    for (i <- 0 until 200) {
      batchContent(0)._2 should equal("Test Data ")
    }
    batchContent.length shouldBe(200)

  }


}
