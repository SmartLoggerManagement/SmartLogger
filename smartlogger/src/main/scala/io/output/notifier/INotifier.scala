package output.notifier

/**
  * Defines a notifier.
  *
  * A notifier aims at broadcasting a specific alert
  * to a given list of recipients, in order
  * to warn them from the analysis's results.
  *
  * @author Franck Caron
  * @since SmartLogger 0.1
  * @version 1.0
  */
trait INotifier {
  // ATTRIBUTES
  /**
    * All recipients which will be alerted by this notifier.
    *
    * The information described by these strings depends on the kind of
    * this notifier.
    */
  private var recipients: Seq[String] = Seq.empty

  /**
    * The text content which will be transmitted by this notifier
    */
  private var text: String = ""


  // REQUESTS
  /**
    * Returns all recipients targeted by this notifier.
    *
    * @return All recipients which are targeted by this notifier
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def getRecipients(): Seq[String] = recipients

  /**
    * Returns the alert text of this notifier.
    *
    * @return The text of any alert sent with this notifier
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def getText(): String = text


  // COMMANDS
  /**
    * Define all recipients which are targeted by this notifier.
    *
    * @param recipients
    * A sequence of Strings describing the concerned contacts.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def setRecipients(recipients: Seq[String]) = {
    this.recipients = recipients
  }

  /**
    * Set the new text which describes the problem
    *
    * @param text
    * The text of this notification
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def setText(text: String) = {
    this.text = text
  }

  /**
    * Send the message to all recipients targeted by this notifier.
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def send(): Unit
}
