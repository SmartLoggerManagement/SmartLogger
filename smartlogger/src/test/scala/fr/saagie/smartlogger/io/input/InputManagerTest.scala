package fr.saagie.smartlogger.io.input

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

/**
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class InputManagerTest extends FeatureSpec with GivenWhenThen with Matchers {
  private val SLEEP_TIME = 1000
  private val SERVER_URL = "http://127.0.0.1:8088/smartlogger/"

  private val input = new InputManager


  feature("InputManager use to retrieve information from HTTP Request") {
    scenario("The client send by PUT a log at SmartLogger.") {
      Given("Open connection")
      input open
      val clt: ClientTest = new ClientTest()


      When("Send a log by PUT")
      clt.sendPutRequest("Test Data", SERVER_URL + "analyze")
      Thread.sleep(SLEEP_TIME)

      Then("Retrieve log from LogBatch")
      val batchContent = LogBatch.getBatch()

      And("and check the result.")
      batchContent should not be empty
      batchContent.head should equal("Test Data")
      input close
    }

    scenario("Used Gatling for Stress test") {
      input open
      val clt: ClientTest = new ClientTest()
      val maxOpenedFiles = 200 // max number of files that can be opened on test environment at one time
      for (i <- 0 until maxOpenedFiles) {
        clt.sendPutRequest("" + i, SERVER_URL + "analyze")
      }
      Thread.sleep(SLEEP_TIME)
      var batchContent = LogBatch.getBatch()
      println(batchContent)
      for (i <- 0 until maxOpenedFiles) {
        batchContent(i) should equal("" + i)
      }
      batchContent.length shouldBe maxOpenedFiles
      input close
    }

    scenario("Request throws a 404 not found") {
      Given("Instantiate client and open connection.")
      input open
      val clt: ClientTest = new ClientTest()

      When("Send a request POST")
      clt.sendPostRequest("Test Data", SERVER_URL + "analyze")

      Then("Retrieve information from LogBatch")
      Thread.sleep(SLEEP_TIME)
      val batchContent = LogBatch.getBatch()

      And("Batch content is empty")
      batchContent shouldBe empty

      input close
    }

    scenario("Client is opened, closed, then opened once more with a request sent after each. The batch should only contain 2 requests") {
      Given("Instantiate client and open connection.")
      input open
      val clt: ClientTest = new ClientTest()

      When("Send three PUT requests")
      clt.sendPutRequest("Test Data1", SERVER_URL + "analyze")
      Thread.sleep(SLEEP_TIME)
      input.close()
      Thread.sleep(SLEEP_TIME)
      clt.sendPutRequest("Closed Request", SERVER_URL + "analyze")
      clt.sendPutRequest("Closed Request2", SERVER_URL + "analyze")
      Thread.sleep(SLEEP_TIME)
      input.open()
      Thread.sleep(SLEEP_TIME)
      clt.sendPutRequest("Test Data2", SERVER_URL + "analyze")
      clt.sendPutRequest("Test Data3", SERVER_URL + "analyze")
      Thread.sleep(SLEEP_TIME)

      Then("Retrieve information from LogBatch")
      val batchContent = LogBatch.getBatch()

      And("Batch content isn't empty")
      println("=================")
      println(batchContent)
      batchContent should not be empty
      batchContent.size shouldBe (3)
      batchContent.head shouldBe ("Test Data1")
      batchContent(1) shouldBe ("Test Data2")
      batchContent(2) shouldBe ("Test Data3")

      input close
    }
  }
}
