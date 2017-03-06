package output

import com.ponkotuy.slack.SlackClient

/**
  * Created by Jordan Baudin on 06/03/17.
  */
class SlackSender(apiKey:String) extends INotifier {

  val s = new SlackClient(apiKey)

  /**
    * Send the message into a communication flux.
    *
    * @param text
    * Content to send.
    * @since SmartLogger 1.0
    * @version 1.0
    */
  override def send(text: String): Unit = {
    s.chat.postMessage("ChannelToDefineInJSON", "List of users to notify" + text)
  }
}
