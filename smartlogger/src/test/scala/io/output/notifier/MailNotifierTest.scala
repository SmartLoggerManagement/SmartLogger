package output.notifier

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}

/**
  * @author Nicolas GILLE
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class MailNotifierTest extends FunSuite with Matchers {
  test("Test mail sending") {
    var recipient: Seq[String] = Seq.empty
    recipient = recipient.+:("franck.caron76@gmail.com")
    recipient = recipient.+:("nic.gille@gmail.com")

    val notifier: MailNotifier = new MailNotifier
    notifier.setText("Test avec .properties")
    notifier.setRecipients(recipient)
    notifier.send()
  }
}

