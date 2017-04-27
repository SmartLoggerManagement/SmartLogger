package fr.saagie.smartlogger.db

import fr.saagie.smartlogger.db.model.DAOData
import fr.saagie.smartlogger.db.model.attributes.{Attribute, AttributeFactory}
import fr.saagie.smartlogger.utils.IPropertiesManager

import scala.collection.mutable.Map

/**
  * This trait is use to create an object use to interact with database and fill a POJO object type.
  *
  * If you have an object present on database and you would interact with it.
  * You can extend this trait and fill the method query and execute to create, request, update or delete
  * an object on the Database Table.
  *
  * @author Nicolas GILLE, Franck CARON
  * @since SmartLogger 0.2
  * @version 1.0
  */
trait DAO[T <: DAOData] {
  // REQUESTS
  /**
    * Provides the name of the database's table in which data will be stored
    */
  def getTableName(): String

  /**
    * Provides the properties manager, which contains all relative data in
    * order to establish connections.
    */
  def getProperties(): IPropertiesManager

  /**
    * Provides the name of kind of factor, which is used to build new attributes
    */
  def getAttributeFactory(): AttributeFactory


  /**
    * Tells if the table used to store all data managed through this DAO,
    * is already defined in the database
    */
  def exists(): Boolean

  /**
    * Returns all pieces of data contained in the database
    */
  def get(): Seq[T] = get(null, null)
  /**
    * Returns all pieces of data contained in the database which verify
    * the given condition.
    * This condition describes the content of the WHERE clause
    * in a SQL SELECT request.
    *
    * @param condition The content of the WHERE clause
    *                  in the SQL SELECT prepared statement to send
    * @param args The attributes as (label, data) pairs,
    *             used to complete the prepared statement
    */
  def get(condition: String, args: Seq[Attribute[_]]): Seq[T]


  // COMMANDS
  /**
    * Builds the DAO's associated table
    */
  def build(): Unit

  /**
    * Destroy the DAO's associated table
    */
  def drop(): Unit

  /**
    * Insert a new element in the DAO's associated table
    *
    * @param elt The element to insert inside the table
    */
  def insert(elt: T): Unit

  /**
    * Update some elements in the DAO's associated table
    *
    * Elements are chosen by describing a WHERE clause.
    *
    * @param set A map of all element's attributes which will be altered to
    *            have the same value as the one specified in this map
    *            (Defines the SET part of the query)
    * @param condition Defines the condition in order to alter a specific part
    *                  of all elements stored in the table. Arguments are specified later
    *                  in the where parameter, and need to be represented in the query
    *                  as a '?' (Because of the use of prepared statements)
    * @param where Defines all values in the condition, values needs to be defined
    *              in the order of values's definition
    */
  def update(set: Map[String, Attribute[_]], condition: String, where: Seq[Attribute[_]]): Unit


  // TOOLS
  /**
    * Method call to build a new instance of type T.
    * Used for query calls, in order to build the resulting
    * sequence.
    */
  protected def newInstance(): T

  /**
    * Method call when you request the Database.
    *
    * This method open a connection to the Database and execute the query passed on parameter.
    * It return a Seq of Log or an empty Seq if the request return nothing log.
    *
    * @param query
    *   The query at execute by the method with the following format : SELECT id, log FROM DATABASE_NAME.
    *   It represents a prepared statement, which means that all values must be
    *   replaced by a '?' in the query
    * @param args
    *   All arguments which will be used to complete the "prepared" part
    *   of the query.
    * @return
    *   Return an instance of Seq who contain all logs retrieve from the Database or an empty Sequence.
    */
  protected def executeQuery(query: String, args: Seq[Attribute[_]]): Seq[T] = {
    // Initialize Database connection, create the statement, and run the query
    val connection = DbConnector.connect(getProperties())
    val statement = connection.prepareStatement(query)

    // Adding arguments to statement
    if (args != null) {
      var k = 1
      for (arg <- args) {
        arg.write(statement, k)
        k += 1
      }
    }

    // Executing the query
    val result = statement.executeQuery()

    // Extracting data
    var seq: Seq[T] = Seq.empty
    while (result.next()) {
      val obj = newInstance()

      // Reading attributes value
      for (key <- obj.attributes.keys) {
        obj.attributes(key).read(result, key)
      }
      seq = seq.+:(obj)
    }

    // Finally, we close the connection
    result.close()
    DbConnector.close(connection)

    return seq
  }
  protected def executeQuery(query: String): Seq[T] = executeQuery(query, null)

  /**
    * Method call when you interact with elements present on the Database.
    *
    * This method open a connection to the Database and execute the query passed on parameter.
    * The query accepted by the method are the query who don't return any information after operation
    * like INSERT, INSERT OVERWRITE, ...
    * If you would return information from the database,
    * you can use <pre>request(query: String): Seq[(Log)]</pre> method.
    *
    * @param query
    *   The query at execute on the Database. Currently, the request is a request about :
    *   INSERT, INSERT OVERWRITE, UPDATE, ...
    *   It represents a prepared statement, which means that all values must be
    *   replaced by a '?' in the query
    * @param args
    *   All arguments which will be used to complete the "prepared" part
    *   of the query.
    */
  protected def execute(query: String, args: Seq[Attribute[_]]): Unit = {
    // Initializing statement.
    val connection = DbConnector.connect(getProperties())
    val statement = connection.prepareStatement(query)

    // Adding arguments to statement
    if (args != null) {
      var k = 1
      for (arg <- args) {
        arg.write(statement, k)
        k += 1
      }
    }

    // Executing the query
    statement.execute()

    // Finally, we close the connection
    DbConnector.close(connection)
  }
  protected def execute(query: String): Unit = execute(query, null)
}
