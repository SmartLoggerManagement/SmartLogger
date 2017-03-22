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

  /**
    * Send the message into a communication flux.
    *
    * @since SmartLogger 1.0
    * @version 1.0
    */
  override def send(): Unit = {
    s.chat.postMessage("#testsmartlogger", "@madzinah @kero76 " + text)
  }
}
