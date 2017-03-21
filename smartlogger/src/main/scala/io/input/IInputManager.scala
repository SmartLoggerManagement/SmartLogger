package input

/**
  *
  * @author Nicolas Gille
  * @since SmartLogger 0.1
  * @version 1.0
  */
trait IInputManager {

  /**
    * Open a connection on a specific port and interface.
    *
    * Interface and port are specified in an external configuration file.
    * So this method bind a socket into a specific port and listen all requests received by this sport.
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def open()

  /**
    * Closing the connection.
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def close()

}
