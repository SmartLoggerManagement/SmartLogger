import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class InputManagerTest extends FunSuite {

  test("testConnect") {
    println("test\n")
    val im: InputManager = new InputManager
    im.connect()
    Thread.sleep(10000)
  }

}
