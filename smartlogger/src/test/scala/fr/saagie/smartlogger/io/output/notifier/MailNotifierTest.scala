package fr.saagie.smartlogger.io.output.notifier

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
  * @author Nicolas GILLE
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class MailNotifierTest extends FeatureSpec with GivenWhenThen with Matchers {
  feature("This class test the MailNotifier by sending 3 e-mails") {
    scenario("Send a mail at 3 users.") {
      Given("Declare list of users to send email.")
      var recipient: Seq[String] = Seq.empty
      recipient = recipient.+:("franck.caron76@gmail.com")
      recipient = recipient.+:("nic.gille@gmail.com")
      recipient = recipient.+:("madzinah@gmail.com")

      val notifier: MailNotifier = new MailNotifier
      notifier.setText("<p>Email with body</p>")
      notifier.setRecipients(recipient)

      When("send email")
      notifier.send()

      Then("Mail should be sent")
      assert(true)
    }

    scenario("Send a mail to an invalid address.") {
      Given("Declare list of the invalid user.")
      var recipient: Seq[String] = Seq.empty
      recipient = recipient.+:("IamInvalidUsermail")
      recipient = recipient.+:("InvalidUserMail.check.check@gmail.com")

      val notifier: MailNotifier = new MailNotifier
      notifier.setText("<p>Email with body</p>")
      notifier.setRecipients(recipient)

      Then("Mail can't be sent. Error")
      intercept[Exception] {

        When("Send mail")
        notifier.send()

      }
    }
  }
}

