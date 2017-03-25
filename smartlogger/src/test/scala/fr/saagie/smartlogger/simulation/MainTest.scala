package fr.saagie.smartlogger.simulation

import fr.saagie.smartlogger.io.input.ClientTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}

/**
  * Created by teegreg on 07/03/17.
  */
@RunWith(classOf[JUnitRunner])
class MainTest extends FunSuite with Matchers{
  test("test normal behavior") {

    val main = new StubSmartlogger
    main.main(null)
    Thread.sleep(1000)

    val clt : ClientTest = new ClientTest()
    clt.sendRequest("Serveur 1 : Tue 7 March 14:46:39 CET 2017 : Temperature = 55°C ", "http://127.0.0.1:8088/smartlogger")
    clt.sendRequest("Serveur 1 : Tue 7 March 14:47:39 CET 2017 : Temperature = 57°C ", "http://127.0.0.1:8088/smartlogger")
    clt.sendRequest("Serveur 1 : Tue 7 March 14:48:39 CET 2017 : Temperature = 59°C ", "http://127.0.0.1:8088/smartlogger")
    clt.sendRequest("Serveur 1 : Tue 7 March 14:49:39 CET 2017 : Temperature = 62°C ", "http://127.0.0.1:8088/smartlogger")
    clt.sendRequest("Serveur 1 : Tue 7 March 14:50:39 CET 2017 : Temperature = 85°C ", "http://127.0.0.1:8088/smartlogger")

    Thread.sleep(21000)

    var res = main.getResult
    res should not be empty
    res = res.sortWith(_._3 > _._3)

    main.closeInput()
    main.stopSchedule()
  }
}
