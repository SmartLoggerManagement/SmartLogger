package output

/**
  *
  * @author Jordan Baudin, Nicolas GILLE
  * @since SmartLogger 1.0
  * @version 1.0
  */
class Observer extends IObserver {

  var notifiers:Seq[INotifier] = Seq.empty

  /**
    * @inheritdoc
    */
  override def addNotifier(notifier: INotifier): Unit = {
    if (notifier != null && !notifiers.contains(notifier)) {
      notifiers = notifiers.:+(notifier)
    }
  }

  /**
    * @inheritdoc
    */
  override def addNotifiers(notifiers: Seq[INotifier]): Unit = {
      for (n <- notifiers) {
        if (!this.notifiers.contains(n)) {
          this.notifiers = notifiers.:+(n)
        }
      }
  }

  /**
    * @inheritdoc
    */
  override def removeNotifier(notifier: INotifier): Unit = {
    this.notifiers = this.notifiers.filter(_ == notifier)
  }

  /**
    * @inheritdoc
    */
  override def notifyAll(content: String): Unit = {
    for (n <- notifiers) {
      n.send(content)
    }
  }
}
