package fr.saagie.smartlogger.db

import java.sql.ResultSet
import java.util.UUID
import model.Log
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
  override def get(): Seq[Log] = get(null, null)
  /**
    * @inheritdoc
    */
  override def get(condition: String, args: Seq[Any]): Seq[Log] = {
    // Retrieving data
    var resultSet:ResultSet = null
    var sql = "SELECT * FROM " + getTableName()
    if (condition != null) {
      sql += " WHERE " + condition
      resultSet = query(sql, args)

    } else {
      resultSet = query(sql)
    }

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
  override def build(): Unit = {
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
  override def drop(): Unit = {
    // Destroying table
    execute("DROP TABLE " + getTableName() + ";")
  }

  /**
    * @inheritdoc
    */
  override def insert(elt: Log): Unit = {
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
  override def update(elt: Log): Unit = {
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
  override def delete(elt: Log): Unit = {
    // Building request
    val sql = "DELETE FROM " + getTableName() + " WHERE id=?;"
    var params: Seq[Any] = Seq.empty
    params = params.+(elt.getId.toString)

    // Building table
    execute(sql, params)
  }

  def main(args: Array[String]): Unit = {
    LogDAO.build()
  }
}
