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
  def addNotifier(notifier: INotifier): Unit

  /**
    * Notify all notifier previously insert.
    * @since SmartLogger 1.0
    * @version 1.0
    */
  def notifyAll(): Unit
}
