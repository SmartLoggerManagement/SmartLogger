package mail

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}
import output.notifier.MailNotifier

/**
  * Created by Kero76 on 08/03/17.
  */
@RunWith(classOf[JUnitRunner])
class MailNotifierTest extends FunSuite with Matchers {
  test("Test mail sending") {
    var recipient: Seq[String] = Seq.empty
    //recipient = recipient.+:("franck.caron76@gmail.com")
    recipient = recipient.+:("nic.gille@gmail.com")

    val notifier: MailNotifier = new MailNotifier
    notifier.setText("Hello world!")
    notifier.setRecipients(recipient)
    notifier.send()
  }
}

