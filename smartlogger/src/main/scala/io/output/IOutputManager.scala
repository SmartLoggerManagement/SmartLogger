package output

/**
  * Created by Kero76 on 15/02/17.
  */
trait IOutputManager {

  /**
    *
    * @param content
    *   Content to send.
    * @since SmartLogger 1.0
    * @version 1.0
    */
  def send(content: String) : Unit
}
