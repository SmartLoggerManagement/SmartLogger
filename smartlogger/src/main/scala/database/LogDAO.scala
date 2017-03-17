import java.sql.{SQLException, SQLTimeoutException}
import java.util.UUID

import database.model.Log

/**
  * It's an implementation of the trait DAO use to represent Log object from the Impala Database.
  * It implement the method query and execute for interact with the Database.
  * It can be possible to retrieve information from the Database with the method query
  * and interact with element on the database thanks to the method execute.
  *
  * @author Nicolas GILLE
  * @see DAO
  * @see DbConnector
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

    try {
      // Initialize Database connection, create the statement, and run the select query
      val statement = DbConnector.openConnection.createStatement
      val resultSet = statement.executeQuery(query)

      // Loop on each result and fill an object Log.
      while (resultSet.next) {
        val id  = resultSet.getString("id")
        val log = resultSet.getString("log")
        result = result.+:(new Log(UUID.fromString(id), log))
      }
    } catch {
      case sqlTimeoutException: SQLTimeoutException => sqlTimeoutException.printStackTrace
      case sqlException:        SQLException        => sqlException.printStackTrace
    } finally {
      // Finally, we close the connection
      DbConnector.closeConnection
    }
    return result
  }

  /**
    * @inheritdoc
    */
  override def execute(query: String): Unit = {
    try {
      // Initialize Database connection, create the statement, and run the insert query.
      val statement = DbConnector.openConnection.createStatement
      statement.execute(query)
    } catch {
      case sqlTimeoutException: SQLTimeoutException => sqlTimeoutException.printStackTrace
      case sqlException:        SQLException        => sqlException.printStackTrace
    } finally {
      // Finally, we close the connection
      DbConnector.closeConnection
    }
  }
}
