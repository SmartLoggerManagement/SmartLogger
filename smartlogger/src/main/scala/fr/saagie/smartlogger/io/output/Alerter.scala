package fr.saagie.smartlogger.io.output

import fr.saagie.smartlogger.io.output.notifier.INotifier

/**
  *
  * @author Jordan Baudin
  * @since SmartLogger 0.1
  * @version 1.0
  */
class Alerter extends IAlerter {

  var notifiers:Seq[INotifier] = Seq.empty

  /**
    * Add a notifier to the Alerter.
    *
    * @param notifier
    *   Notifier to add.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  override def addNotifier(notifier: INotifier): Unit = {
    if (notifier != null && !notifiers.contains(notifier)) {
      notifiers = notifiers.:+(notifier)
    }
  }

  /**
    * Add a Sequence of notifier at alert.
    *
    * @param notifiers
    *   Notifiers to add.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  override def addNotifiers(notifiers: Seq[INotifier]): Unit = {
    for (n <- notifiers) {
      this.addNotifier(n)
    }
  }

  /**
    * Remove a specific notifier.
    *
    * @param notifier
    *   Notifier to remove.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  override def removeNotifier(notifier: INotifier): Unit = {
      notifiers = notifiers.filterNot(_ == notifier)
  }

  /**
    * Notify all notifiers previously added.
    *
    * @param content
    *   content to send.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  override def notifyAll(content: String): Unit = {
    for (n <- notifiers) {
      n.setText(content)
      n.send()
    }
  }
}
