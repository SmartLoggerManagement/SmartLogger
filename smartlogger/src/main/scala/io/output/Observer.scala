package output

/**
  *
  * @author Jordan Baudin, Nicolas GILLE
  * @since SmartLogger 1.0
  * @version 1.0
  */
class Observer extends IAlerter {

  var notifiers:Seq[Notifier] = Seq.empty

  /**
    * @inheritdoc
    */
  override def addNotifier(notifier: Notifier): Unit = {
    if (notifier != null && !notifiers.contains(notifier)) {
      notifiers = notifiers.:+(notifier)
    }
  }

  /**
    * @inheritdoc
    */
  override def addNotifiers(notifiers: Seq[Notifier]): Unit = {
      for (n <- notifiers) {
        if (!this.notifiers.contains(n)) {
          this.notifiers = notifiers.:+(n)
        }
      }
  }

  /**
    * @inheritdoc
    */
  override def removeNotifier(notifier: Notifier): Unit = {
    this.notifiers = this.notifiers.filter(_ == notifier)
  }

  /**
    * @inheritdoc
    */
  override def notifyAll(content: String): Unit = {
    for (n <- notifiers) {
      n.setText(content)
      n.send
    }
  }
}
