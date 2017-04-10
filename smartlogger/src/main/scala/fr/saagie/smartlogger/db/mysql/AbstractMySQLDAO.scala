package fr.saagie.smartlogger.db.mysql

import java.sql.ResultSet

import fr.saagie.smartlogger.db.DAO
import fr.saagie.smartlogger.db.model.DAOData
import fr.saagie.smartlogger.db.model.attributes.Attribute

import scala.collection.mutable.Map

/**
  *
  * @author Nicolas GILLE
  * @since SmartLogger 0.2
  * @version 1.0
  */
abstract protected[mysql] class AbstractMySQLDAO[T <: DAOData] extends DAO[T] {
  // REQUESTS
  /**
    * @inheritdoc
    */
  override def get(condition: String, args: Map[String, Attribute[_ <: Object]]): Seq[T] = {
    // Retrieving data
    var resultSet:ResultSet = null
    var sql = "SELECT * FROM " + getTableName()

    // Adding condition, if the condition was defined
    if (condition != null && args != null) {
      sql += " WHERE " + condition
    }

    // Extracting data
    return executeQuery(sql)
  }


  // COMMANDS
  /**
    * @inheritdoc
    */
  override def build(): Unit = {
    // Building request with the following format : CREATE TABLE IF NOT EXISTS db_name.table_name (column1 type1, ...);
    val sql = new StringBuilder
    sql.append("CREATE TABLE IF NOT EXISTS ").append(LogDAO.getDatabaseName()).append(".").append(getTableName()).append(" (")

    // Adding data content
    val obj = newInstance()
    val keys = obj.attributes.keys.iterator
    while (keys.hasNext) {
      val key = keys.next()
      sql.append(key).append(" ").append(obj.attributes(key).getType())

      // Adding separator if it isn't the last attribute
      if (keys.hasNext) sql.append(", ")
    }
    sql.append(");")

    // Building table
    execute(sql.toString())
  }

  /**
    * @inheritdoc
    */
  override def drop(): Unit = {
    // Destroying table
    execute("DROP TABLE IF EXISTS " + LogDAO.getDatabaseName() + "." + getTableName() + ";")
  }

  /**
    * @inheritdoc
    */
  override def insert(elt: T): Unit = {
    // Building request
    val sql = new StringBuilder
    sql.append("INSERT INTO ").append(getTableName())
    sql.append(" VALUES (")

    // Creating n-uplet
    var k = elt.attributes.size
    while (k > 0) {
      sql.append('?')
      if (k > 1) sql.append(',')
    }
    sql.append(");")

    // Building table
    execute(sql.toString(), elt.attributes)
  }

  /**
    * @inheritdoc
    */
  override def update(elt: T, args: Map[String, Attribute[_ <: Object]]): Unit = {
    throw new UnsupportedOperationException
  }
}
