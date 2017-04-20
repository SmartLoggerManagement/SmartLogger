package fr.saagie.smartlogger.db

import java.sql.{Connection, DriverManager}

import fr.saagie.smartlogger.utils.IPropertiesManager

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
    *   <li>url : The URL of the database's location, formatted for mysql connection : <pre>jdbc:mysql://localhost/mysql</pre>.</li>
    *   <li>username : Username of registered database's user</li>
    *   <li>password : Password of registered database's user</li>
    * </ul>
    *
    * Notices that a connection properties file, can contains other informations
    * that can be used later by DAOs.
    *
    * @param properties The property manager which contains all needed infos to have
    *                   an access to the extern database
    *
    * @return
    *   Return the connection of the database.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def connect(properties: IPropertiesManager): Connection = {
    // Connect to database
    val driver   = properties.get("driver")
    val url      = properties.get("url")
    val username = properties.get("username")
    val password = properties.get("password")

   // Building the connection
    Class.forName(driver)
    DriverManager.getConnection(url, username, password)
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
