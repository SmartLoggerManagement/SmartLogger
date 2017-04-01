package fr.saagie.smartlogger.simulation

import fr.saagie.smartlogger.io.input.ClientTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FeatureSpec, GivenWhenThen, Matchers}

/**
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class SmartLoggerTestIntegrationML_I extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfter {
  // Variable StubSL.
  val main = new StubMainIntegrationML_I

  // Execute after each test.
  after {
    main.closeInput()
    main.stopSchedule()
  }

  feature("This class tests the integration of the machine learning module and the input module") {
    info("This comports the input and machine learning module. Output being replaced by a stub")

    scenario("The scenario consist in test the normal behavior of SmartLogger application") {
      Given("Instantiate the stub use to simulate smartLogger.")
      main.main(null)

      // Stop time for 1 second
      Thread sleep 1000

      val uri = "http://127.0.0.1:8088/smartlogger" // URI to send message.
      val clt: ClientTest = new ClientTest()
      clt.sendPutRequest("Serveur 1 : Tue 7 March 14:46:39 CET 2017 : Temperature = 55°C ", uri)
      clt.sendPutRequest("Serveur 1 : Tue 7 March 14:47:39 CET 2017 : Temperature = 57°C ", uri)
      clt.sendPutRequest("Serveur 1 : Tue 7 March 14:48:39 CET 2017 : Temperature = 59°C ", uri)
      clt.sendPutRequest("Serveur 1 : Tue 7 March 14:49:39 CET 2017 : Temperature = 62°C ", uri)
      clt.sendPutRequest("Serveur 1 : Tue 7 March 14:50:39 CET 2017 : Temperature = 85°C ", uri)
      Thread.sleep(21000)

      When("Receive Result from SmartLogger.")
      var res = main.getResult

      Then("The result should not be empty")
      res should not be empty
      res = res.sortWith(_._3 > _._3)

      And ("and it contain 5 elements")
      res.size should equal(5)

      And("and 2 logs have a level of criticality at 1")
      res.head._3 should equal(1.0)
      res(1)._3 should equal(1.0)

      And ("and 3 logs have a level of criticality at 0")
      res(2)._3 should equal(0.0)
      res(3)._3 should equal(0.0)
      res(4)._3 should equal(0.0)
    }
  }
}


