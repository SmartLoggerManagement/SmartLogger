package output

import com.ponkotuy.slack.SlackClient
import com.ponkotuy.slack.methods.Chat
import com.ponkotuy.slack.responses.PostMessageResponse
import org.mockito.Mockito
import org.scalatest.{FeatureSpec, GivenWhenThen}
import org.scalatest.mockito.MockitoSugar
import output.notifier.SlackNotifier
import util.EncryptedPropertiesManager

/**
  * Created by Jordan Baudin on 12/03/17.
  */
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

      val testSlackSender = new SlackNotifier(apiKey)
      testSlackSender.s = clientMock

      When("The sending function is called")

      testSlackSender setText message
      testSlackSender send

      Then("It must send a message")
      Mockito.verify(chatMock, Mockito.times(2)).postMessage("#testsmartlogger", "@madzinah @kero76 " + message)
    }


    scenario("A message needs to be send with a correct API key") {

      Given("A Slack client with a correct API key")
      val properties = new EncryptedPropertiesManager
      properties.load("src/test/resources/bundle.properties")
      val apiKey = properties.get("$apiKey")
      val message = "TestingMessage"

      val testSlackSender = new SlackNotifier(apiKey)

      When("The sending function is called")

      testSlackSender setText message
      testSlackSender send

      Then("It must send a message")

    }

  }
}
