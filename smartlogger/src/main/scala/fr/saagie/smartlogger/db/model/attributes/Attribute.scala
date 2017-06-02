package fr.saagie.smartlogger.db.model.attributes

import java.sql.{PreparedStatement, ResultSet, SQLException}

/**
  * Represents an attribute.
  * An attribute is used to define accurately the action to do
  * on the INSERT/GET request, by providing the 'type' support
  * to these operations.
  *
  * @author Franck CARON
  * @since SmartLogger 0.2
  * @version 1.0
  */
abstract class Attribute[T](firstValue: T) {
  // ATTRIBUTE
  /** The value of this attribute */
  protected var obj: T = firstValue


  // ACCESSORS
  /**
    * Retrieves the value of this attribute
    */
  def value: T = obj


  // REQUESTS
  /**
    * Retrieves the type used inside the external data table of this attribute
    */
  def getType(): String

  /**
    * Reads the value of the attribute from the result set,
    * at the actual line and at the column specified by the given label.
    *
    * @param result      The resultSet used for the reading
    * @param columnLabel The column's name
    * @throws SQLException
    * An error has occurred when the data was retrieved
    */
  def read(result: ResultSet, columnLabel: String)


  // COMMANDS
  /**
    * Sets a new value for the attribute
    */
  def set(obj: T) = this.obj = obj

  /**
    * Defines the parameter inside the given prepared statement.
    * It aims at writing new values inside data tables' rows.
    *
    * @param state          The prepared request which needs to be defined
    * @param parameterIndex The index of the parameter to define
    * @throws SQLException
    * An error has occurred when the data was retrieved
    */
  def write(state: PreparedStatement, parameterIndex: Int)
}
