package fr.saagie.smartlogger.db.pgsql

import java.sql.ResultSet

import fr.saagie.smartlogger.db.DAO
import fr.saagie.smartlogger.db.model.attributes.{Attribute, AttributeFactory}
import fr.saagie.smartlogger.db.model.DAOData
import fr.saagie.smartlogger.utils.Properties

import scala.collection.mutable.Map

/**
  * Default implementation of a DAO, which is connected to
  * a PGSQL database system.
  *
  * @tparam T The type of the manipulated data
  * @author Franck CARON
  * @since SmartLogger 0.2
  * @version 1.0
  */
abstract protected[pgsql] class AbstractPGDAO[T <: DAOData] extends DAO[T] {
  // REQUESTS
  /**
    * @inheritdoc
    */
  override def getProperties() = Properties.DB_TEST

  /**
    * @inheritdoc
    */
  override def getAttributeFactory(): AttributeFactory = AttrPGSQLFactory

  /**
    * @inheritdoc
    */
  override def exists(): Boolean = {
    throw new UnsupportedOperationException
  }

  /**
    * @inheritdoc
    */
  override def get(condition: String, args: Seq[Attribute[_ <: Object]]): Seq[T] = {
    // Retrieving data
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
    execute(sql.toString(), elt.attributes.values.toSeq)
  }

  /**
    * @inheritdoc
    */
  override def update(set: Map[String, Attribute[_ <: Object]], condition: String, where: Seq[Attribute[_ <: Object]]): Unit = {
    // Filtering input with defined keys
    val obj = newInstance()
    set.filterKeys(key => obj.attributes.contains(key))

    // Controls if the number of parameters is correct
    if (set.isEmpty) return

    // Building request
    val sql = new StringBuilder
    sql.append("UPDATE ").append(getTableName()).append(" SET ")
    var args: Seq[Attribute[_ <: Object]] = Seq.empty

    // Defining SET part of the request
    val keys = set.keys.iterator
    while (keys.hasNext) {
      val key = keys.next()
      sql.append(key).append("=?")
      args = args.+:(set(key))

      // Adding delimeter if it isn't the last argument
      if (keys.hasNext) sql.append(", ")
    }

    // Adding condition to define WHERE part of the request
    if (condition != null && where != null) {
      sql.append(" WHERE ").append(condition)
      args = args.++(where)
    }

    // Executing request
    execute(sql.toString(), args)
  }
}
