package dao

import database.dao.Log

/**
  * This trait is use to create an object use to interact with database and fill a POJO object type.
  *
  * If you have an object present on database and you would interact with it.
  * You can extend this trait and fill the method query and execute to create, request, update or delete
  * an object on the Database Table.
  *
  * @author Nicolas GILLE
  * @since SmartLogger 0.2
  * @version 1.0
  */
trait DAO {

  /**
    * Method call when you request the Database.
    *
    * This method open a connection to the Database and execute the query passed on parameter.
    * It return a Seq of Log or an empty Seq if the request return nothing log.
    *
    * @param query
    *   The query at execute by the method with the following format : SELECT id, log FROM DATABASE_NAME.
    * @return
    *   Return an instance of Seq who contain all logs retrieve from the Database or an empty Sequence.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def query(query: String): Seq[(Log)]

  /**
    * Method call when you interact with elements present on the Database.
    *
    * This method open a connection to the Database and execute the query passed on parameter.
    * The query accepted by the method are the query who don't return any information after operation
    * like INSERT, INSERT OVERWRITE, ...
    * If you would return information from the database, you can use <pre>request(query: String): Seq[(Log)]</pre> method.
    *
    * @param query
    *   The query at execute on the Database. Currently, the request is a request about : INSERT, INSERT OVERWRITE, UPDATE, ...
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def execute(query: String): Unit
}
