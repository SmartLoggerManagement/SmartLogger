package output

/**
  *
  * @author Jordan Baudin, Nicolas GILLE
  * @since SmartLogger 1.0
  * @version 1.0
  */
trait INotifier {

  /**
    * Send the message into a communication flux.
    *
    * @param text
    *   Content to send.
    *
    * @since SmartLogger 1.0
    * @version 1.0
    */
  def send(text: String) : Unit
}
