package fr.saagie.smartlogger.io.output.notifier

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FeatureSpec, FunSuite, GivenWhenThen, Matchers}

/**
  * @author Nicolas GILLE
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class MailNotifierTest extends FeatureSpec with GivenWhenThen with Matchers {
  feature("This class test the MailNotifier by sending 3 e-mails")

  scenario("Test mail sending") {
    Given("A notifier and some e-mails")
    var recipient: Seq[String] = Seq.empty
    recipient = recipient.+:("franck.caron76@gmail.com")
    recipient = recipient.+:("nic.gille@gmail.com")
    recipient = recipient.+:("madzinah@gmail.com")

    val notifier: MailNotifier = new MailNotifier
    notifier.setText("Test with .properties file")
    notifier.setRecipients(recipient)
    When("send")
    notifier.send()
    Then("Mail should be sent")

  }
}

