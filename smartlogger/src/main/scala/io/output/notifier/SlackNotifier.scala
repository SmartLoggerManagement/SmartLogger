package output.notifier

import com.ponkotuy.slack.SlackClient
import util.Properties

/**
  * Defines a notifier which emits its alerts through Slack app.
  *
  * @author Jordan BAUDIN
  * @since SmartLogger 0.2
  * @version 1.0
  */
class SlackNotifier extends INotifier {
  // ATTRIBUTES
  /**
    * The client used to interact with the Slack API
    */
  private val slackClient = new SlackClient(Properties.SLACK.get("$apiKey"))

  // COMMANDS
  /**
    * Send the message into a communication flux.
    *
    * @since SmartLogger 0.2
    * @version 1.0
    */
  override def send(): Unit = {
    // Building message
    val build = new StringBuilder
    for (recipient <- getRecipients()) {
      build.append("@" + recipient)
    }
    build.append(getText())

    // Posting to the Slack thread
    slackClient.chat.postMessage(Properties.SLACK.get("thread"), build.toString())
  }
}
