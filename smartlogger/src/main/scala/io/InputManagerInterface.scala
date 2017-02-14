
/**
  * Created by Kero76 on 09/02/17.
  */
trait InputManagerInterface {

  /**
    * Open a connection on a specific port and interface.
    *
    * Interface and port are specified in an external configuration file.
    * So this method bind a socket into a specific port and listen all requests received by this sport.
    *
    * @since
    * @version
    */
  def connect()

  /**
    * Close the connection previously open by the method connect.
    *
    * This method close the socket only if the socket is open and listen specific port.
    *
    * @see connect()
    * @since
    * @version
    */
  def disconnect()

  /**
    * Convert data receive from the HTTP flux.
    *
    * @since
    * @version
    */
  def convert()

  /**
    * Send the data previously convert to the Machine Learning to process train and predict method.
    *
    * @since
    * @version
    */
  def sendData()
}
