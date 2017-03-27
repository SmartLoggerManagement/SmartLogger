package fr.saagie.smartlogger.io.output.notifier

import com.ponkotuy.slack.SlackClient
import fr.saagie.smartlogger.utils.Properties

/**
  * Defines a notifier which emits its alerts through Slack app.
  *
  * @author Jordan BAUDIN, Franck CARON
  * @since SmartLogger 0.2
  * @version 1.0
  */
class SlackNotifier(apiKey:String) extends INotifier {
  // ATTRIBUTES
  /**
    * The client used to interact with the Slack API
    */

  private val slackClient = new SlackClient(apiKey)

  /**
    * The Slack channel in which alerts will be published
    */
  private var channel: String = Properties.SLACK.get("thread")

  def this() {
    this(Properties.SLACK.get("$apiKey"))
  }

  // COMMANDS
  /**
    * Defines a new channel in which alerts will be pushed
    *
    * @param channel
    *     The new channel for alert publishing
    */
  def setChannel(channel : String) = this.channel = channel


  /**
    * Send the message into a communication flux.
    *
    * @since SmartLogger 0.2
    * @version 1.0
    */
  override def send(): Unit = {
    // Building message
    val message = getRecipients().mkString("@", " @", " ") + getText()

    // Posting to the Slack thread
    slackClient.chat.postMessage(channel, message)
  }
}
