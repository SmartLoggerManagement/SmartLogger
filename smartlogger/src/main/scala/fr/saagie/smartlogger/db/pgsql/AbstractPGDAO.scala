package fr.saagie.smartlogger.db.pgsql

import java.sql.ResultSet

import fr.saagie.smartlogger.db.DAO
import fr.saagie.smartlogger.db.model.attributes.{Attribute, AttributeFactory}
import fr.saagie.smartlogger.db.model.DAOData

import scala.collection.mutable.Map

abstract protected[pgsql] class AbstractPGDAO[T <: DAOData] extends DAO[T] {
  // REQUESTS
  /**
    * Provides the name of kind of factor, which is used to build new attributes
    */
  def getAttributeFactory(): AttributeFactory = AttrPGSQLFactory

  /**
    * @inheritdoc
    */
  override def get(condition: String, args: Map[String, Attribute[_ <: Object]]): Seq[T] = {
    // Retrieving data
    var resultSet:ResultSet = null
    var sql = "SELECT * FROM " + getTableName()

    // Adding condition, if the condition was defined
    if (condition != null && args != null) {
      sql += " WHERE " + condition + ";"
      return executeQuery(sql, args)
    }
    sql += ";"

    // Extracting data
    return executeQuery(sql)
  }


  // COMMANDS
  /**
    * @inheritdoc
    */
  override def build(): Unit = {
    // Building request
    val sql = new StringBuilder
    sql.append("CREATE TABLE ").append(getTableName()).append(" (")

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
    execute("DROP TABLE " + getTableName() + ";")
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
      sql.append("?")
      if (k > 1) sql.append(",")
      k -= 1
    }
    sql.append(");")

    // Building table
    execute(sql.toString(), elt.attributes)
  }

  /**
    * @inheritdoc
    */
  override def update(elt: T, args: Map[String, Attribute[_ <: Object]]): Unit = {
    // Controls if the number of parameters is correct
    if (elt.attributes.size >= args.size) return

    // Building request
    val sql = new StringBuilder
    sql.append("UPDATE ").append(getTableName()).append(" SET ")

    // Defining SET part of the request
    var attrs = elt.attributes.filterKeys(key => !args.contains(key))
    var keys = attrs.keys.iterator
    while (keys.hasNext) {
      val key = keys.next()
      sql.append(key).append("=?")
      if (keys.hasNext) sql.append(", ")
    }

    // Adding condition
    sql.append(" WHERE ")
    keys = elt.attributes.keys.iterator
    while (keys.hasNext) {
      val key = keys.next()
      sql.append(key).append("=?")
      if (keys.hasNext) sql.append(" AND ")
    }
    sql.append(");")

    // Executing request
    val temp = Map.empty
    execute(sql.toString(), temp.++(elt.attributes).++(attrs))
  }
}
