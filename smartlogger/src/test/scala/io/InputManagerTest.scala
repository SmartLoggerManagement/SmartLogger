import input.InputManager
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import AutomaticClient

@RunWith(classOf[JUnitRunner])
class InputManagerTest extends FunSuite {

  /*Tests effectué par Jordan (?)
  * Révision :
   */
  test("testConnectFunctionality") {
    println("test\n")
    val im: InputManager = new InputManager
    im.connect()
    Thread.sleep(10000)
  }
  /*Tests effectué par Camille
  * Révision :
  * Comments : For future reference, make a mock usage of your methods. Will make tests
   */

  /* Tests connect()
  * expected behaviour : connects and receives data
  * Comments : How does Input Manager mock receive data?
   */
  test("testConnectAndReceive") {
    val im: InputManager = new InputManager
    val client: AutomaticClient = new AutomaticClient
    im.connect()
    client.main()
    im.message() should equal("I am data!")
  }

  /*Tests convert()
  * expected behaviour : im.message != im.oldMessage
  * Comments : If we know how converted message is structured,
  * this test can be more precise.
   */
  test("testConvert") {
    val im: InputManager = new InputManager
    im.message = new String("I am Data")
    im.convert()
    assert(im.message != (new String("I am Data")))
  }

  /* Tests isConnect()
  * expected behavior : true
  * Comments : Minimal impact?
   */
  test("testIsConnected") {
    val im: InputManager = new InputManager
    im.connect()
    assert(im.isConnected())
    im.disconnect()
    assert(!im.isConnected())
  }

}
