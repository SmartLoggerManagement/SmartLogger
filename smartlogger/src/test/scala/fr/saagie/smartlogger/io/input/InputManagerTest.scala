package fr.saagie.smartlogger.io.input

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

/**
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class InputManagerTest extends FunSuite with Matchers  {
  private val SLEEP_TIME = 2000

  test("NormalBehavior") {
    val input = new InputManager
    input.open()
    val clt : ClientTest = new ClientTest()
    clt.sendPutRequest("Test Data", "http://127.0.0.1:8088/smartlogger")
    Thread.sleep(SLEEP_TIME)
    val batchContent = LogBatch.getBatch()
    batchContent.head._2 should equal("Test Data")
    input.close()
  }

  test("ChargeTest") {
    val input = new InputManager
    input.open()
    val clt: ClientTest = new ClientTest()
    val maxOpenedFiles = 200// max number of files that can be opened on test environment at one time
    for (i <- 0 until maxOpenedFiles) {
      clt.sendPutRequest("" + i, "http://127.0.0.1:8088/smartlogger")
    }
    Thread.sleep(SLEEP_TIME)
    var batchContent = LogBatch.getBatch()
    batchContent = batchContent.sortWith{_._2.toInt < _._2.toInt}
    println(batchContent)
    for (i <- 0 until maxOpenedFiles) {
      batchContent(i)._2 should equal("" + i)

    }
    batchContent.length shouldBe maxOpenedFiles
    input.close()
  }

  test("404 test") {
    val input = new InputManager
    input.open()
    val clt : ClientTest = new ClientTest()
    clt.sendPostRequest("Test Data", "http://127.0.0.1:8088/smartlogger")
    Thread.sleep(SLEEP_TIME)
    val batchContent = LogBatch.getBatch()
    batchContent shouldBe empty
    input.close()
  }
}
