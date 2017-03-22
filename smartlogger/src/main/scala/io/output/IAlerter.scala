package output

import output.notifier.INotifier

/**
  *
  * @author Nicolas GILLE, Jordan BAUDIN
  * @since SmartLogger 0.1
  * @version 1.0
  */
trait IAlerter {

  /**
    * Add a notifier to the Alerter.
    *
    * @param notifier
    * Notifier to add.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def addNotifier(notifier: INotifier): Unit

  /**
    * Add a Sequence of notifier at alert.
    *
    * @param notifiers
    * Notifiers to add.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def addNotifiers(notifiers: Seq[INotifier]): Unit

  /**
    * Remove a specific notifier.
    *
    * @param notifier
    * Notifier to remove.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def removeNotifier(notifier: INotifier): Unit

  /**
    * Notify all notifiers previously added.
    *
    * @param content
    * content to send.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def notifyAll(content: String): Unit
}
