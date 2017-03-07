import input.{InputManager, LogBatch}
import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

/**
  * Created by teegreg on 07/03/17.
  */
@RunWith(classOf[JUnitRunner])
class InputManagerTest extends FunSuite with Matchers  {

  private val SLEEP_TIME = 2000

  test("NormalBehavior") {
    InputManager.open()
    val clt : ClientTest = new ClientTest()
    clt.sendRequest("Test Data", "http://127.0.0.1:8080/smartlogger")
    Thread.sleep(SLEEP_TIME)
    val batchContent = LogBatch.getBatch()
    batchContent.head._2 should equal("Test Data")
  }

  test("ChargeTest") {
    InputManager.open()
    val clt: ClientTest = new ClientTest()
    val maxOpenedFiles = 200// max number of files that can be opened on test environment at one time
    for (i <- 0 until maxOpenedFiles) {
      clt.sendRequest("" + i, "http://127.0.0.1:8080/smartlogger")
    }
    Thread.sleep(SLEEP_TIME)
    var batchContent = LogBatch.getBatch()
    batchContent = batchContent.sortWith{_._2.toInt < _._2.toInt}
    println(batchContent)
    for (i <- 0 until maxOpenedFiles) {
      batchContent(i)._2 should equal("" + i)

    }
    batchContent.length shouldBe maxOpenedFiles

  }

  test("404 test") {
    InputManager.open()
    val clt : ClientTest = new ClientTest()
    clt.sendPost("Test Data", "http://127.0.0.1:8080/smartlogger")
    Thread.sleep(SLEEP_TIME)
    val batchContent = LogBatch.getBatch()
    batchContent shouldBe empty
  }



}
