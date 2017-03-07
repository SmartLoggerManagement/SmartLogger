package dao

/**
  * Created by Kero76 on 07/03/17.
  *
  * @author Nicolas GILLE
  * @since SmartLogger 0.2
  * @version 1.0
  */
class Log (val i: Long, val l: String) {

  /**
    * The id of the log.
    */
  private val _id: Long = i

  /**
    * The content of the log.
    */
  private val _log: String = l

  /**
    * Get the unique identifier of the Log
    *
    * @return
    *   Return the unique identifier of the log.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def id = _id

  /**
    * Get the content of the log.
    *
    * @return
    *   Return the content of the log.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def log = _log

  /**
    * Return a view of the object InputSettings.
    *
    * @return
    *   Return a view of the object InputSettings.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  override def toString: String = this._id + " : " + this._log
}
