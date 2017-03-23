package output.notifier

import com.ponkotuy.slack.SlackClient

/**
  *
  * @author Jordan BAUDIN
  * @param apiKey
  * @since SmartLogger 0.2
  * @version 1.0
  *
  */
class SlackNotifier(apiKey:String) extends INotifier {
  var s = new SlackClient(apiKey)
  var channel: String = ""

  def setChannel(c : String) = channel = c

  /**
    * @inheritdoc
    */
  override def send(): Unit = {
    val allRecipients = recipients.mkString(" ")
    s.chat.postMessage(channel, allRecipients + " " + text)
  }
}
