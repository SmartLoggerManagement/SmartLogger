package fr.saagie.smartlogger.db

import fr.saagie.smartlogger.db.model.Log

/**
  * Provides a builder in order to produce DAO for all data types
  * which are manipulated by the application's model.
  *
  * @author Franck CARON
  * @see DAO
  * @see DbConnector
  * @since SmartLogger 0.2
  * @version 1.0
  */
trait DAOBuilder {
  /**
    * Produces a new DAO in order to handle Logs.
    */
  def getLogDAO(): DAO[Log]
}
