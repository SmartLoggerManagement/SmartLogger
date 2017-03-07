import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}


/**
  * Created by teegreg on 07/03/17.
  */
@RunWith(classOf[JUnitRunner])
class SmartLogger$Test extends FunSuite with Matchers{




  test("test normal behavior") {

    SmartLogger.main(null)

    Thread.sleep(1000)


    val clt : ClientTest = new ClientTest()
    clt.sendRequest("Serveur 1 : Tue 7 March 14:46:39 CET 2017 : Temperature = 55°C ", "http://127.0.0.1:8080/smartlogger")
    clt.sendRequest("Serveur 1 : Tue 7 March 14:47:39 CET 2017 : Temperature = 57°C ", "http://127.0.0.1:8080/smartlogger")
    clt.sendRequest("Serveur 1 : Tue 7 March 14:48:39 CET 2017 : Temperature = 59°C ", "http://127.0.0.1:8080/smartlogger")
    clt.sendRequest("Serveur 1 : Tue 7 March 14:49:39 CET 2017 : Temperature = 62°C ", "http://127.0.0.1:8080/smartlogger")

    Thread.sleep(20000)

    var res = SmartLogger.getResult
    res should not be empty
    println(res)
    res = res.sortWith(_._3 > _._3)
    res.head._3 should equal(1.0)
    res(1)._3 should equal(0.0)
    res(2)._3 should equal(0.0)
    res(3)._3 should equal(0.0)



  }

}
