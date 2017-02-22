package output

/**
  * Created by Jordan Baudin on 20/02/17.
  */
class Alerter extends IAlerter {

  var notifiers:Seq[INotifier] = Seq.empty

  /**
    * Add a notifier to the Alerter.
    *
    * @param notifier
    * Notifier to add.
    * @since SmartLogger 1.0
    * @version 1.0
    */
  override def addNotifier(notifier: INotifier): Unit = {
    if (notifier != null && !notifiers.contains(notifier)) {
      notifiers = notifiers.:+(notifier)
    }
  }

  /**
    * Notify all notifiers previously added.git pull
    *
    *
    * @param content
    * content to send.
    * @since SmartLogger 1.0
    * @version 1.0
    */
  override def alertAll(content: String): Unit = {
    for (n <- notifiers) {
      n.send(content)
    }
  }
}
