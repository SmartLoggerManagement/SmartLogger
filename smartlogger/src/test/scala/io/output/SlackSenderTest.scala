package output

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
  * Created by Jordan Baudin on 06/03/17.
  */
@RunWith(classOf[JUnitRunner])
class SlackSenderTest extends FunSuite {

  test("testSend") {
    val slacker = new SlackSender("test")

    slacker.send("Coucou")
  }

}
