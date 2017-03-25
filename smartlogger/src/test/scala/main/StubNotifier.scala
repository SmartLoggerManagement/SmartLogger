import output.notifier.INotifier

/**
  * Created by teegreg on 08/03/17.
  */
class StubNotifier extends INotifier {
  /**
    * Send the message to all recipients targeted by this notifier.
    */
  override def send(): Unit = {
    println("There is a problem with : ")
    println(this.getText())
  }
}
