package dao

import java.sql.{Connection, DriverManager, SQLException, SQLTimeoutException}

import database.dao.Log

/**
  * It's an implementation of the trait DAO use to represent Log object from the Impala Database.
  * It implement the method query and execute for interact with the Database.
  * It can be possible to retrieve information from the Database with the method query
  * and interact with element on the database thanks to the method execute.
  *
  * @author Nicolas GILLE
  * @see DAO
  * @since SmartLogger 0.2
  * @version 1.0
  */
object LogDAO extends DAO {
  /**
    * @inheritdoc
    */
  override def query(query: String): Seq[(Log)] = {
    // Sequence with all Log at return after SELECT query.
    var result: Seq[Log] = Seq.empty

    // connect to the database named "mysql" on the localhost
    val driver   = "com.mysql.jdbc.Driver"
    val url      = "jdbc:mysql://localhost/mysql"
    val username = "root"
    val password = "root"

    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement
      val resultSet = statement.executeQuery(query)

      // Loop on each result and fill an object Log.
      while (resultSet.next) {
        val id  = resultSet.getString("id")
        val log = resultSet.getString("log")
        result = result.+:(new Log(id.toLong, log))
      }
    } catch {
      case sqlTimeoutException: SQLTimeoutException => sqlTimeoutException.printStackTrace
      case sqlException:        SQLException        => sqlException.printStackTrace
    }
    connection.close()
    return result
  }

  /**
    * @inheritdoc
    */
  override def execute(query: String): Unit = {
    // connect to the database named "mysql" on the localhost
    val driver   = "com.mysql.jdbc.Driver"
    val url      = "jdbc:mysql://localhost/mysql"
    val username = "root"
    val password = "root"

    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the insert query.
      val statement = connection.createStatement
      statement.execute(query)

    } catch {
      case sqlTimeoutException: SQLTimeoutException => sqlTimeoutException.printStackTrace
      case sqlException:        SQLException        => sqlException.printStackTrace
    }
    connection.close()
  }
}
