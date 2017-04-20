package fr.saagie.smartlogger.db.pgsql

import fr.saagie.smartlogger.db.model.Log
import fr.saagie.smartlogger.db.{DAO, DAOBuilder}

/**
  * Builds DAOs that use a connection with a PGSQL database system.
  *
  * @author Franck CARON
  * @see DAOBuilder
  * @since SmartLogger 0.2
  * @version 1.0
  */
object PGDAOBuilder extends DAOBuilder {
  /**
    * Produces a new DAO in order to handle Logs.
    */
  override def getLogDAO(): DAO[Log] = new AbstractPGDAO[Log] {
    override protected def newInstance(): Log = new Log(getAttributeFactory())
    override def getTableName(): String = "Log"
  }
}
