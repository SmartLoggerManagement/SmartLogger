package fr.saagie.smartlogger.db.model

import attributes.Attribute
import scala.collection.mutable.Map

/**
  * Represents an object that can be stored in the database,
  * through the use of a specific DAO.
  *
  * @author Franck CARON
  * @since SmartLogger 0.2
  * @version 1.0
  */
trait DAOData {
  // ATTRIBUTES
  /**
    * Defines the attributes which compose the piece of data.
    * These are stored into a map, in order to label attributes
    * with a name, which acts as a key
    *
    * @return Returns the corresponding map
    */
  val attributes: Map[String, Attribute[_ <: Object]] = initialize()

  // TOOLS
  /**
    * Initializes the attributes' map
    */
  protected def initialize(): Map[String, Attribute[_ <: Object]]
}
