package fr.saagie.smartlogger.db.model

import java.util.UUID

/**
  * The log's model used for storage in the Database.
  *
  * A Log is modelised by two attributes :
  * <ul>
  *   <li>id : Unique identifier of the log which representing by an object UUID</li>
  *   <li>log : The content of the Log.</li>
  * </ul>
  *
  * @author Nicolas GILLE
  * @since SmartLogger 0.2
  * @version 1.0
  */
class Log (val uuid: UUID, val l: String) {
  /**
    * The id of the log.
    *
    * This log identifier is an UUID generate totally randomly
    * to avoid auto-incrementation not present on Impala.
    */
  private val _id: UUID = uuid

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
  def getId = _id

  /**
    * Get the content of the log.
    *
    * @return
    *   Return the content of the log.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def getLog = _log

  /**
    * Return a view of the object InputSettings.
    *
    * @return
    *   Return a view of the object InputSettings.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  override def toString: String = this._id.toString + " : " + this._log
}
