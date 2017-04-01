package fr.saagie.smartlogger.io.output

import fr.saagie.smartlogger.io.output.notifier.{MailNotifier, SlackNotifier}
import org.easymock.EasyMock
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FeatureSpec, GivenWhenThen, Matchers}
import org.scalatest.junit.JUnitRunner


/**
  * @author Nicolas Gille
  * @since SmartLogger 0.2
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class AlerterTest extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfter {
  // Alerter use in all tests.
  var alerter : Alerter = _

  // Function call before each test.
  before {
    alerter = new Alerter
  }

  feature("Test the behavior of the Alerter class") {
    scenario("Add a Notifier on Alerter") {
      Given("Initialize Alerter and Notifier")
      val notifier = new MailNotifier("")

      When("Add notifier on alerter")
      alerter.addNotifier(notifier)

      Then("Number of notifier is equal at 1")
      alerter.notifiers.size should equal(1)
    }

    scenario("Add a sequence of notifiers on Alerter and check the number of notifiers already present") {
      Given("Initialize Alerter and a sequence of Notifiers")
      val notifiers = Seq.empty
      notifiers.+:(Seq(
        new MailNotifier(""),
        new MailNotifier(""),
        new SlackNotifier(""),
        new MailNotifier(""),
        new SlackNotifier("")
      ))

      When("Add notifiers on alerter")
      alerter.addNotifiers(notifiers)

      Then("The number of notifier present on alerter are equals to the size of the sequence notifiers")
      alerter.notifiers.size should equal(notifiers.size)
    }

    scenario("Add 3 time the same instance of notifier") {
      Given("Instantiate notifier")
      val notifier = new MailNotifier()

      When("Add the mail notifier on alerter")
      alerter.addNotifier(notifier)

      Then("The alerter contain only 1 instance of notifier")
      alerter.notifiers.size should equal(1)
    }

    scenario("Add 2 time the same sequence of Notifiers") {
      Given("Instantiate notifiers")
      val notifiers = Seq.empty
      notifiers.+:(Seq(
        new MailNotifier(""),
        new MailNotifier(""),
        new SlackNotifier(""),
        new MailNotifier(""),
        new SlackNotifier("")
      ))
      alerter.addNotifiers(notifiers)

      When("Add to new the sequence of notifier.")
      alerter.addNotifiers(notifiers)

      Then("The number of notifiers are the same as the sequence of the notifiers added.")
      alerter.notifiers.size should equal(notifiers.size)
    }

    scenario("Remove a notifier present on alerter") {
      Given("Instantiate a notifier and add it on alerter")
      val notifier = new MailNotifier("")
      alerter.addNotifier(notifier)

      When("Remove the notifier")
      alerter.removeNotifier(notifier)

      Then("Alerter doesn't contain notifier")
      alerter.notifiers.size should equal(0)
    }

    scenario("Remove a notifier not insert previously on alerter") {
      Given("Instantiate a notifier")
      val notifier = new SlackNotifier("")

      When("Remove a notifier not insert previously")
      alerter.removeNotifier(notifier)

      Then("Nothing append, the alerter not contains notifier")
      alerter.notifiers.size should equal(0)
    }

    scenario("Remove a notifier insert at the start of the sequence of notifier") {
      Given("Instantiate notifiers")
      val slack = new SlackNotifier("")
      val notifiers = Seq.empty
      notifiers.+:(Seq(
        slack,
        new MailNotifier(""),
        new MailNotifier(""),
        new MailNotifier(""),
        new SlackNotifier("")
      ))
      alerter.addNotifiers(notifiers)

      When("Remove a notifier not insert previously")
      alerter.removeNotifier(slack)

      Then("Nothing append, the alerter not contains notifier")
      alerter.notifiers.size should equal(4)
    }

    scenario("Remove a notifier insert in the middle of the sequence of notifier") {
      Given("Instantiate notifiers")
      val slack = new SlackNotifier("")
      val notifiers = Seq.empty
      notifiers.+:(Seq(
        new MailNotifier(""),
        new MailNotifier(""),
        slack,
        new MailNotifier(""),
        new SlackNotifier("")
      ))
      alerter.addNotifiers(notifiers)

      When("Remove a notifier not insert previously")
      alerter.removeNotifier(slack)

      Then("Nothing append, the alerter not contains notifier")
      alerter.notifiers.size should equal(4)
    }

    scenario("Remove a notifier insert at the end of the sequence of notifier") {
      Given("Instantiate notifiers")
      val slack = new SlackNotifier("")
      val notifiers = Seq.empty
      notifiers.+:(Seq(
        slack,
        new MailNotifier(""),
        new MailNotifier(""),
        new MailNotifier(""),
        new SlackNotifier("")
      ))
      alerter.addNotifiers(notifiers)

      When("Remove a notifier not insert previously")
      alerter.removeNotifier(slack)

      Then("Nothing append, the alerter not contains notifier")
      alerter.notifiers.size should equal(4)
    }

    scenario("Add a notifier on the alerter and notify it") {
      Given("Instantiate a notifier and add it on alerter")
      val notifierMock = EasyMock.createMock(classOf[MailNotifier])
      notifierMock.send()
      EasyMock.expect(notifierMock.send())

      alerter.addNotifier(notifierMock)
      val notification = "I'm notify"

      When("Alerter notify notifier with the following message : 'I'm notify'")
      alerter.notifyAll(notification)

      Then("The method send was call once")
      EasyMock.expectLastCall.once()
      And(" and the alerter contain 1 notifier")
      alerter.notifiers.size should equal(1)
    }

    scenario("Add two notifiers and alerter notify them") {
      Given("Instantiate two notifiers and add them on alerter")
      val mock1 = EasyMock.createMock(classOf[MailNotifier])
      mock1.send()
      EasyMock.expect(mock1.send())

      val mock2 = EasyMock.createMock(classOf[MailNotifier])
      mock2.send()
      EasyMock.expect(mock2.send())

      alerter.addNotifier(mock1)
      alerter.addNotifier(mock2)
      val notification = "I'm notify"

      When("Alerter notify notifiers with the following message : 'I'm notify'")
      alerter.notifyAll(notification)

      Then("The method send called twice")
      EasyMock.expectLastCall.times(2)
      And(" and the alerter contain 1 notifier")
      alerter.notifiers.size should equal(2)
    }

    scenario("Add one mail notifier and one slack notifier and alerter notify them") {
      Given("Instantiate two notifiers and add them on alerter")
      val mock1 = EasyMock.createMock(classOf[MailNotifier])
      mock1.send()
      EasyMock.expect(mock1.send())

      val mock2 = EasyMock.createMock(classOf[SlackNotifier])
      mock2.send()
      EasyMock.expect(mock2.send())

      alerter.addNotifier(mock1)
      alerter.addNotifier(mock2)
      val notification = "I'm notify"

      When("Alerter notify notifiers with the following message : 'I'm notify'")
      alerter.notifyAll(notification)

      Then("The method send called twice")
      EasyMock.expectLastCall.times(2)
      And(" and the alerter contain 1 notifier")
      alerter.notifiers.size should equal(2)
    }

    scenario("Notify when the alerter not contains notifiers") {
      Given("Generate the notification at send")
      val notification = "I'm notify"

      When("Alerter notify all notifiers.")
      alerter.notifyAll(notification)

      Then("Nothing appended because the alerter don't notify notifier.")
    }
  }
}
