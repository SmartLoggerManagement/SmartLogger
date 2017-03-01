package input


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
  def open()

}
