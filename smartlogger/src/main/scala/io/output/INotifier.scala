package output

import javax.mail.MessagingException

/**
  * Created by Kero76 on 15/02/17.
  */
trait INotifier {

  /**
    * Send the message into a communication flux.
    *
    * @param text
    *   Content to send.
    *   @throws MessagingException
    * @since SmartLogger 1.0
    * @version 1.0
    */
  def send(text: String) : Unit
}
