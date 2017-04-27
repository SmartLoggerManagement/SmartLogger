package fr.saagie.smartlogger.db.mysql

import fr.saagie.smartlogger.db.model.Log
import fr.saagie.smartlogger.db.{DAO, DAOBuilder}

/**
  * Builds DAOs that use a connection with a MySQL database system.
  *
  * @author Franck CARON
  * @see DAOBuilder
  * @since SmartLogger 0.2
  * @version 1.0
  */
object MySQLDAOBuilder extends DAOBuilder {
  /**
    * Produces a new DAO in order to handle Logs.
    */
  override def getLogDAO(): DAO[Log] = new AbstractMySQLDAO[Log] {
    override def newInstance(): Log = new Log(getAttributeFactory())
    override def getTableName(): String = "Log"
  }
}
