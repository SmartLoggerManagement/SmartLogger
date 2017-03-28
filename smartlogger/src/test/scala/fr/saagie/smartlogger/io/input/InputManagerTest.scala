package fr.saagie.smartlogger.io.input

import org.junit.runner.RunWith
import org.scalatest.{FeatureSpec, FunSuite, GivenWhenThen, Matchers}
import org.scalatest.junit.JUnitRunner

/**
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class InputManagerTest extends FeatureSpec with GivenWhenThen with Matchers {
  private val SLEEP_TIME = 2000

  feature("InputManager use to retrieve information from HTTP Request") {
    scenario("The client send by PUT a log at SmartLogger.") {
      Given("Open connection")
      val input = new InputManager
      input open
      val clt: ClientTest = new ClientTest()
      Thread.sleep(SLEEP_TIME)

      When("Send a log by PUT")
      clt.sendPutRequest("Test Data", "http://127.0.0.1:8088/smartlogger")

      Then("Retrieve log from LogBatch")
      val batchContent = LogBatch.getBatch()

      And("and check the result.")
      batchContent.head._2 should equal("Test Data")
      input.close()
    }

    scenario("Used Gatling for Stress test") {
      val input = new InputManager
      input.open()
      val clt: ClientTest = new ClientTest()
      val maxOpenedFiles = 200 // max number of files that can be opened on test environment at one time
      for (i <- 0 until maxOpenedFiles) {
        clt.sendPutRequest("" + i, "http://127.0.0.1:8088/smartlogger")
      }
      Thread.sleep(SLEEP_TIME)
      var batchContent = LogBatch.getBatch()
      batchContent = batchContent.sortWith {
        _._2.toInt < _._2.toInt
      }
      println(batchContent)
      for (i <- 0 until maxOpenedFiles) {
        batchContent(i)._2 should equal("" + i)

      }
      batchContent.length shouldBe maxOpenedFiles
      input.close()
    }

    scenario("Request throws a 404 not found") {
      Given("Instantiate client and open connection.")
      val input = new InputManager
      input.open()
      val clt: ClientTest = new ClientTest()

      When("Send a request POST")
      clt.sendPostRequest("Test Data", "http://127.0.0.1:8088/smartlogger")

      Then("Retrieve information from LogBatch")
      Thread.sleep(SLEEP_TIME)
      val batchContent = LogBatch.getBatch()

      And("Batch content is empty")
      batchContent shouldBe empty
      input.close()
    }
  }
}
