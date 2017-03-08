package output

/**
  * This objects aims at notifying a given list of recipient in order
  * to warn them from the analysis's results.
  *
  * @since SmartLogger 1.0
  * @version 1.0
  */
trait Notifier {
  // ATTRIBUTES
  /** All adresses which will be alerted by this notifier */
  var recipients:Seq[String] = Seq.empty

  /** The text content which will be transmitted by this notifier */
  var text:String = ""

  // COMMANDS
  /**
    * Define all recipients which are targeted by this notifier.
    *
    * @param recipients
    *     A sequence of Strings describing the concerned contacts.
    */
  def setRecipients(recipients:Seq[String]) = {
    this.recipients = recipients
  }

  /**
    * Set the new text which describes the problem
    *
    * @param text
    *    The text of this notification
    */
  def setText(text:String) = {
    this.text = text
  }

  /**
    * Send the message to all recipients targeted by this notifier.
    */
  def send() : Unit
}
