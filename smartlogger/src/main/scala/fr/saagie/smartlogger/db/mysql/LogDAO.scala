package fr.saagie.smartlogger.db.mysql

import fr.saagie.smartlogger.db.model.Log
import fr.saagie.smartlogger.db.model.attributes.AttributeFactory

/**
  * Created by Kero76 on 10/04/17.
  */
object LogDAO extends AbstractMySQLDAO[Log] {
  // REQUESTS
  /**
    * @inheritdoc
    */
  override def getTableName(): String = "Log"

  /**
    * Return the name of the database.
    *
    * @return
    *   The name of the Database.
    */
  def getDatabaseName(): String = "smartlogger"

  /**
    * Provides the name of kind of factor, which is used to build new attributes
    */
  override def getAttributeFactory(): AttributeFactory = AttrMySQLFactory

  /**
    * Method call to build a new instance of type T.
    * Used for query calls, in order to build the resulting
    * sequence.
    */
  override protected def newInstance(): Log = new Log(getAttributeFactory())
}
