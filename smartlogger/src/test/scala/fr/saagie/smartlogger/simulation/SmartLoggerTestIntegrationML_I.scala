package fr.saagie.smartlogger.simulation

import fr.saagie.smartlogger.io.input.ClientTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class SmartLoggerTestIntegrationML_I extends FeatureSpec with GivenWhenThen with Matchers {
  feature("This class tests the integration of the machine learning module and the input module") {
    info("This comports the input and machine learning module. Output being replaced by a stub")

    scenario("The scenario consist in test the normal behavior of SmartLogger application") {
      Given("Instantiate the stub use to simulate smartLogger.")
      val main = new StubMainIntegrationML_I
      main.main(null)
      val clt: ClientTest = new ClientTest()
      val uri = "http://127.0.0.1:8088/smartlogger"
      // URI to send message.
      val logs: Seq[String] = Seq.empty // All logs at send.
      logs.++(Seq(
        "Serveur 1 : Tue 7 March 14:46:39 CET 2017 : Temperature = 55°C ",
        "Serveur 1 : Tue 7 March 14:47:39 CET 2017 : Temperature = 57°C ",
        "Serveur 1 : Tue 7 March 14:48:39 CET 2017 : Temperature = 59°C ",
        "Serveur 1 : Tue 7 March 14:49:39 CET 2017 : Temperature = 62°C ",
        "Serveur 1 : Tue 7 March 14:50:39 CET 2017 : Temperature = 85°C "
      ))

      When("We send it 5 logs.")
      for (log <- logs) {
        clt.sendPutRequest(log, uri)
      }

      Thread.sleep(21000)

      Then("The result should not be empty")
      var res = main.getResult
      res should not be empty
      res = res.sortWith(_._3 > _._3)
      And("and contain 2 dangerous one")
      res.head._3 should equal(1.0)
      res(1)._3 should equal(1.0)
      res(2)._3 should equal(0.0)
      res(3)._3 should equal(0.0)
      res(4)._3 should equal(0.0)

      And("and close all open stream.")
      main.closeInput()
      main.stopSchedule()
    }
  }
}


