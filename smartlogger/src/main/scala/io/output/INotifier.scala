package output

/**
  * Created by Kero76 on 15/02/17.
  */
trait INotifier {

  /**
    * Send the message into a communication flux.
    *
    * @param content
    *   Content to send.
    * @since SmartLogger 1.0
    * @version 1.0
    */
  def send(content: String) : Unit
}
