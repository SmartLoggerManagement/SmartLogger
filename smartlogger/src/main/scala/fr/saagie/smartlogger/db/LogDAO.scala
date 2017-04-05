package fr.saagie.smartlogger.db

import java.util.UUID

import fr.saagie.smartlogger.db.model.Log

import scala.collection.mutable.Map

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
object LogDAO extends DAO[Log] {
  /**
    * @inheritdoc
    */
  override def getTableName(): String = "LogTable"

  /**
    * Defines the table's columns, the returned map contains all
    * pairs (column name, column type), used to build the DAO's table
    */
  def getAttributeMap() : Map[String, String] = {
    val result: Map[String, String] = Map.empty
    result.put("id", "text")
    result.put("log", "text")

    return result
  }

  /**
    * @inheritdoc
    */
  def get(condition: String): Seq[Log] = {
    // Retrieving data
    val sql = "SELECT * FROM " + getTableName() + " WHERE " + condition + ";"
    val resultSet = query(sql)

    // Extracting data
    var result: Seq[Log] = Seq.empty
    while (resultSet.next) {
      val id  = resultSet.getString("id")
      val log = resultSet.getString("log")
      result = result.+:(new Log(UUID.fromString(id), log))
    }

    return result
  }


  // COMMANDS
  /**
    * @inheritdoc
    */
  def build(): Unit = {
    // Building request
    val attrs = getAttributeMap()
    val sql = new StringBuilder
    sql.append("CREATE TABLE ").append(getTableName()).append(" (")
    for (key <- attrs.keys) {
      sql.append(key).append(" ").append(attrs.get(key))
    }
    sql.append(" );")

    // Building table
    execute(sql.toString())
  }

  /**
    * @inheritdoc
    */
  def drop(): Unit = {
    // Destroying table
    execute("DROP TABLE " + getTableName() + ";")
  }

  /**
    * @inheritdoc
    */
  def insert(elt: Log): Unit = {
    // Building request
    val sql = "INSERT INTO " + getTableName() + " VALUES (?, ?)"
    var params: Seq[Any] = Seq.empty
    params = params.+(elt.getId.toString)
    params = params.+(elt.getLog)

    // Building table
    execute(sql, params)
  }

  /**
    * @inheritdoc
    */
  def update(elt: Log): Unit = {
    // Building request
    val sql = "UPDATE " + getTableName() + " SET log=? WHERE id=?;"
    var params: Seq[Any] = Seq.empty
    params = params.+(elt.getLog)
    params = params.+(elt.getId.toString)

    // Building table
    execute(sql, params)
  }

  /**
    * @inheritdoc
    */
  def delete(elt: Log): Unit = {
    // Building request
    val sql = "DELETE FROM " + getTableName() + " WHERE id=?;"
    var params: Seq[Any] = Seq.empty
    params = params.+(elt.getId.toString)

    // Building table
    execute(sql, params)
  }
}
