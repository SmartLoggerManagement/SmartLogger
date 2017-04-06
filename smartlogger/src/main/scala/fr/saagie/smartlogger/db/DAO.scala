package fr.saagie.smartlogger.db

import java.sql.{ResultSet, SQLException, SQLTimeoutException}

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
trait DAO[T] {
  // REQUESTS
  /**
    * Provides the name of the database's table in which data will be stored
    */
  def getTableName(): String

  /**
    * Returns all pieces of data contained in the database
    *
    * @return
    */
  def get(): Seq[T]
  /**
    * Returns all pieces of data contained in the database which verify
    * the given condition.
    * This condition describes the content of the WHERE clause
    * in a SQL SELECT request.
    *
    * @param condition The content of the WHERE clause
    *                  in the SQL SELECT prepared statement to send
    * @param args The parameters used to complete the prepared statement
    */
  def get(condition: String, args: Seq[Any]): Seq[T]


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
  def update(elt: T): Unit

  /**
    * Delete all elements in the DAO's associated table, which verify
    * the given condition.
    * A condition describes the content of the WHERE clause,
    * in a SQL DELETE request
    *
    * @param elt The element to delete from the database
    */
  def delete(elt: T): Unit


  // TOOLS
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
  protected def query(query: String, args: Seq[Any]): ResultSet = {
    // Sequence with all Log at return after SELECT query.
    var result: ResultSet = null

    try {
      // Initialize Database connection, create the statement, and run the query
      val statement = DbConnector.openConnection.prepareStatement(query)
      var k = 1
      while (k < args.size) {
        statement.setObject(k, args(k))
        k += 1
      }

      result = statement.executeQuery(query)
    } catch {
      case sqlTimeoutException: SQLTimeoutException => sqlTimeoutException.printStackTrace
      case sqlException: SQLException => sqlException.printStackTrace
    } finally {
      // Finally, we close the connection
      DbConnector.closeConnection
    }
    return result
  }
  protected def query(query: String): ResultSet = {
    this.query(query, Seq.empty)
  }

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
  protected def execute(query: String, args: Seq[Any]): Unit = {
    try {
      // Initialize Database connection, create the statement, and run the insert query.
      val statement = DbConnector.openConnection.prepareStatement(query)
      var k = 1
      while (k < args.size) {
        statement.setObject(k, args(k))
        k += 1
      }

      statement.execute()
    } catch {
      case sqlTimeoutException: SQLTimeoutException => sqlTimeoutException.printStackTrace
      case sqlException:        SQLException        => sqlException.printStackTrace
    } finally {
      // Finally, we close the connection
      DbConnector.closeConnection
    }
  }
  protected def execute(query: String): Unit = {
    execute(query, Seq.empty)
  }
}
