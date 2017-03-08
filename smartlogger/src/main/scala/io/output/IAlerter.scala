package output

/**
  * Created by Kero76 on 17/02/17.
  */
trait IAlerter {

  /**
    * Add a notifier to the Alerter.
    *
    * @param notifier
    *   Notifier to add.
    * @since SmartLogger 1.0
    * @version 1.0
    */
  def addNotifier(notifier: Notifier): Unit

  /**
    * Notify all notifiers previously added.
    *
    * @param content
    *   content to send.
    * @since SmartLogger 1.0
    * @version 1.0
    */
  def alertAll(content: String): Unit
}
