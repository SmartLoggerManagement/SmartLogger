package fr.saagie.smartlogger.db

import java.sql.{Connection, DriverManager}

import fr.saagie.smartlogger.utils.Properties

/**
  * DbConnector is an object used to connect SmartLogger with an
  * external Database.
  *
  * @author Nicolas GILLE, Franck CARON
  * @since SmartLogger 0.2
  * @version 1.0
  */
object DbConnector {
  // COMMANDS
  /**
    * Establishes a new connection with the database
    *
    * Retrieves information from the correct bundle properties :
    * <ul>
    *   <li>driver : the complete driver use in DriverManager with the following format : <pre>com.mysql.jdbc.Driver</pre>.</li>
    *   <li>url : URL of the Database, formatted for mysql connection : <pre>jdbc:mysql://localhost/mysql</pre>.</li>
    *   <li>username : Username of resgistered database's user</li>
    *   <li>password : Password of resgistered database's user</li>
    * </ul>
    *
    * @return
    *   Return the connection of the database.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def connect(): Connection = {
    // Retrieve all connection properties
    val properties = Properties.DB_TEST

    // Connect to database
    val driver   = properties.get("driver")
    val url      = properties.get("url")
    val username = properties.get("username")
    val password = properties.get("password")

   // Building the connection
    Class.forName(driver)
    val connection = DriverManager.getConnection(url, username, password)
    return connection
  }

  /**
    * Close the connection.
    *
    * This method close the Database connection if the connection is previously open by the method openConnection.
    * In other case, this method has no effect.
    *
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def close(connection: Connection): Unit = {
    if (connection != null) {
      connection.close()
    }
  }
}
