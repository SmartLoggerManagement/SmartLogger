package fr.saagie.smartlogger.db

import fr.saagie.smartlogger.db.model.DAOData
import fr.saagie.smartlogger.db.model.attributes.{Attribute, AttributeFactory}

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
    * Provides the name of kind of factor, which is used to build new attributes
    */
  def getAttributeFactory(): AttributeFactory

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
  def get(condition: String, args: Map[String, Attribute[_ <: Object]]): Seq[T]


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
    */
  def insert(elt: T): Unit

  /**
    * Update an existing element in the DAO's associated table
    */
  def update(elt: T, args: Map[String, Attribute[_ <: Object]]): Unit


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
    * @since SmartLogger 0.2
    * @version 1.0
    */
  protected def executeQuery(query: String, args: Map[String, Attribute[_ <: Object]]): Seq[T] = {
    println(query)

    // Initialize Database connection, create the statement, and run the query
    val connection = DbConnector.connect()
    val statement = connection.prepareStatement(query)

    // Adding arguments to statement
    if (args != null) {
      var k = 1
      for (key <- args.keys) {
        args(key).write(statement, k)
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
    * @since SmartLogger 0.2
    * @version 1.0
    */
  protected def execute(query: String, args: Map[String, Attribute[_ <: Object]]): Unit = {
    println(query)
    // Initializing statement.
    val connection = DbConnector.connect()
    val statement = connection.prepareStatement(query)

    // Adding arguments to statement
    if (args != null) {
      var k = 1
      for (key <- args.keys) {
        args(key).write(statement, k)
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
