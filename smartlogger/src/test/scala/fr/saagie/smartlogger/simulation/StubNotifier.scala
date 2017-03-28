package fr.saagie.smartlogger.simulation

import fr.saagie.smartlogger.io.output.notifier.INotifier

/** This is an implementation of INotifier
  * aiming for test in a local context
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
class StubNotifier extends INotifier {
  /**
    * Send the message to all recipients targeted by this notifier.
    */

  /**
    * @TODO Add comments
    */
  override def send(): Unit = {
    println("There is a problem with : ")
    println(this.getText())
  }
}
