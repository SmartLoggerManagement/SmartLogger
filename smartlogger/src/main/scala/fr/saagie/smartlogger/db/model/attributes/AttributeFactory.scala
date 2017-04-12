package fr.saagie.smartlogger.db.model.attributes

import java.sql.Timestamp
import java.util.UUID

/**
  * Represents an attribute factory, used to build new attributes
  * for objects stored in data tables.
  * Each AttributeFactory is specialized for a given DBMS system.
  *
  * @author Franck CARON
  * @since SmartLogger 0.2
  * @version 1.0
  */
trait AttributeFactory {
  // METHODS
  /**
    * Creates a new attribute to store CLOBs (Texts without size limits)
    */
  def newCLOB(text: String): Attribute[String]

  /**
    * Creates a new attribute to store CLOBs (Texts without size limits)
    */
  def newUUID(value: UUID): Attribute[UUID]

  /**
    * Creates a new attribute to store Integers
    */
  def newInt(value: Int): Attribute[Int]

  /**
    * Creates a new attribute to store Doubles
    */
  def newDouble(value: Double): Attribute[Double]

  /**
    * Creates a new attribute to store Strings (n chars max.)
    *
    * @param n The size of the string
    */
  def newString(value: String, n: Int): Attribute[String]

  /**
    * Creates a new attribute to store Dates
    */
  def newDate(value: Timestamp): Attribute[Timestamp]
}
