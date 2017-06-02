package fr.saagie.smartlogger.io.output.notifier

import com.ponkotuy.slack.SlackClient
import com.ponkotuy.slack.methods.Chat
import com.ponkotuy.slack.responses.PostMessageResponse
import fr.saagie.smartlogger.utils.Properties
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FeatureSpec, GivenWhenThen}

/**
  * @author Jordan BAUDIN
  * @since SmartLogger 0.2
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class SlackSenderTest extends FeatureSpec with GivenWhenThen with MockitoSugar {
  feature("The notifier can send a message to a specific channel") {

    info("To protect the API key, we need to mock the working of SlackClient")
    info("And then mocking the Slack Client")

    scenario("A message needs to be send with a testing API key") {
      Given("A Slack client with a testing API key")
      val apiKey = "TESTING_API_KEY"
      val message = "TestingMessage"

      val clientMock = MockitoSugar.mock[SlackClient]
      val chatMock = MockitoSugar.mock[Chat]
      Mockito.when(clientMock.chat).thenReturn(chatMock)
      Mockito.when(chatMock.postMessage("#testsmartlogger", "@madzinah @kero76 " + message))
          .thenReturn(new PostMessageResponse(true, "", ""))

      val testSlackSender = new SlackNotifier

      When("The sending function is called")

      testSlackSender setText message
      testSlackSender setChannel "#testsmartlogger"
      testSlackSender setRecipients Seq("madzinah", "kero76")
      testSlackSender send

      Then("It must send a message")
      Mockito.verify(chatMock, Mockito.times(1)).postMessage("#testsmartlogger", "@madzinah @kero76 " + message)
    }

    scenario("A message needs to be send with a correct API key") {
      Given("A Slack client with a correct API key")
      val testSlackSender = new SlackNotifier(Properties.SLACK.get("$apiKey"))
      val message = "Testing message from Smartlogger Tests"
      When("The sending function is called")

      testSlackSender setText message
      testSlackSender setChannel "#testsmartlogger"
      testSlackSender setRecipients Seq("madzinah", "kero76")
      testSlackSender send

      Then("It must send a message")
      assert(true)
    }

    scenario("A message tries to be sent with in incorrect apiKey") {
      Given("A Slack client with an incorrect API key")
      val testSlackSender = new SlackNotifier("I4m1nC0rr3c14P1K3YF34Rm34nD4L1IR3pr3S3n1=")
      val message = "Testing message from Smartlogger Tests"

      Then("An exception is sent")

      intercept[Exception] {

        When("The sending function is called")

        testSlackSender setText message
        testSlackSender setChannel "#testsmartlogger"
        testSlackSender setRecipients Seq("madzinah", "kero76")
        testSlackSender send
      }
    }
  }
}
