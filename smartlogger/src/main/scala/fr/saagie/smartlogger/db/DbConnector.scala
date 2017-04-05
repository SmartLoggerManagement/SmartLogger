package fr.saagie.smartlogger.db

import java.sql.{Connection, DriverManager}

import fr.saagie.smartlogger.utils.Properties

/**
  * DbConnector is a class use to connect SmartLogger with a Database.
  * This class is composed by two methods use to open
  * and close Connection with a Database.
  *
  * @author Nicolas GILLE
  * @since SmartLogger 0.2
  * @version 1.0
  */
object DbConnector {
  // ATTRIBUTE
  /**
    * Object Connection at return when you open a connection on a Database.
    */
  private var connection : Connection = _

  // COMMANDS
  /**
    * Method use to open a connection on a Database.
    *
    * This method retrieve information from the correct bundle properties.
    * These informations are :
    * <ul>
    *   <li>driver : the complete driver use in DriverManager with the following format : <pre>com.mysql.jdbc.Driver</pre>.</li>
    *   <li>url : URL of the Database, with this format for mysql connection : <pre>jdbc:mysql://localhost/mysql</pre>.</li>
    *   <li>username : Username of the user register on Database.</li>
    *   <li>password : Password of the user register on Database.</li>
    * </ul>
    * If the connection is possible, the DriverManager return an instance of Connection.
    *
    * @return
    *   Return the connection of the database.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def openConnection: Connection = {
    // Retrieve all connection properties
    val properties = Properties.DB_TEST

    // Connect to database
    val driver   = properties.get("driver")
    val url      = properties.get("url")
    val username = properties.get("username")
    val password = properties.get("password")

   // Building the connection
    Class.forName(driver)
    return DriverManager.getConnection(url, username, password)
  }

  /**
    * Method use to close Connection when the request is completed.
    *
    * This method close the Database connection if the connection is previously open by the method openConnection.
    * In other case, this method has no effect.
    *
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def closeConnection : Unit = {
    if (connection != null) {
      connection.close()
      connection = null
    }
  }
}
