package fr.saagie.smartlogger.simulation

import fr.saagie.smartlogger.io.input.ClientTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FeatureSpec, FunSuite, GivenWhenThen, Matchers}

/**
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class SmartLoggerTestIntegrationML_I extends FeatureSpec with GivenWhenThen with Matchers {
  feature("This class tests the integration of the machine learning module and the input module")
  info("This comports the input and machine learning module. Output being replaced by a stub")

  scenario("test normal behavior") {
    Given("A stub equivalent of the used main class")
    val main = new StubMainIntegrationML_I
    main.main(null)
    Thread.sleep(1000)

    When("A few request are sent")
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
    res = res.sortWith(_._3 > _._3)
    res.head._3 should equal(1.0)
    res(1)._3 should equal(1.0)
    res(2)._3 should equal(0.0)
    res(3)._3 should equal(0.0)
    res(4)._3 should equal(0.0)

    main.closeInput()
    main.stopSchedule()
  }
}
