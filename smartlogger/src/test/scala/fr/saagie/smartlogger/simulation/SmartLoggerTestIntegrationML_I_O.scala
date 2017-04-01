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
class SmartLoggerTestIntegrationML_I_O extends FeatureSpec with Matchers with GivenWhenThen with BeforeAndAfter {
  // Variable StubSL.
  val main = new StubMainIntegrationML_I

  // Execute after each test.
  after {
    main.closeInput()
    main.stopSchedule()
  }

  feature("This class tests the behavior of the application, regarding the first release") {
    info("This comports the input, machine learning and output modules (Mail only)")

    scenario("The scenario consist in test the normal behavior of SmartLogger application") {
      assert(true)
      Given("Instantiate the stub use to simulate smartLogger.")
      Given("A main class")
      val main = new StubMainIntegrationML_I_O
      main.main(null)
      Thread.sleep(1000)

      When("We send it a few logs")
      val clt : ClientTest = new ClientTest()
      clt.sendPutRequest("Serveur 1 : Tue 7 March 14:46:39 CET 2017 : Temperature = 55°C ", "http://127.0.0.1:8088/smartlogger")
      clt.sendPutRequest("Serveur 1 : Tue 7 March 14:47:39 CET 2017 : Temperature = 57°C ", "http://127.0.0.1:8088/smartlogger")
      clt.sendPutRequest("Serveur 1 : Tue 7 March 14:48:39 CET 2017 : Temperature = 59°C ", "http://127.0.0.1:8088/smartlogger")
      clt.sendPutRequest("Serveur 1 : Tue 7 March 14:49:39 CET 2017 : Temperature = 62°C ", "http://127.0.0.1:8088/smartlogger")
      clt.sendPutRequest("Serveur 1 : Tue 7 March 14:50:39 CET 2017 : Temperature = 85°C ", "http://127.0.0.1:8088/smartlogger")

      Thread.sleep(21000)

      var res = main.getResult

      Then("The result should not be empty, and contain 2 dangerous one")
      res should not be empty

      And("res contains 5 elements")
      res.size should be equals(5)
    }
  }
}
