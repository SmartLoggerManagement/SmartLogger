package fr.saagie.smartlogger.db.pgsql

import fr.saagie.smartlogger.db.model.Log
import fr.saagie.smartlogger.db.model.attributes.AttributeFactory

/**
  * It's an implementation of the trait DAO use to represent Log object from the Impala Database.
  * It implement the method query and execute for interact with the Database.
  * It can be possible to retrieve information from the Database with the method query
  * and interact with element on the database thanks to the method execute.
  *
  * @author Nicolas GILLE, Franck CARON
  * @see DAO
  * @see DbConnector
  * @since SmartLogger 0.2
  * @version 1.0
  */
object LogDAO extends AbstractPGDAO[Log] {
  // REQUESTS
  /**
    * @inheritdoc
    */
  override def getTableName(): String = "Log"

  /**
    * Method call to build a new instance of type T.
    * Used for query calls, in order to build the resulting
    * sequence.
    */
  override protected def newInstance(): Log = new Log(getAttributeFactory())
}
