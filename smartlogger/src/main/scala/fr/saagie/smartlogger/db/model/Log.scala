package fr.saagie.smartlogger.db.model

import java.util.UUID
import fr.saagie.smartlogger.db.model.attributes.{Attribute, AttributeFactory}
import scala.collection.mutable.Map

/**
  * The log's model used for storage in the Database.
  *
  * A Log is represented by two attributes :
  * <ul>
  *   <li>id : Unique identifier of the log which representing by an object UUID</li>
  *   <li>log : The content of the Log.</li>
  * </ul>
  *
  * @author Nicolas GILLE, Franck CARON
  * @since SmartLogger 0.2
  * @version 1.0
  */
class Log(factory: AttributeFactory) extends DAOData {
  // REQUESTS
  /**
    * Get the unique identifier of the Log
    *
    * @return
    *   Return the unique identifier of the log.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def getId: UUID = attributes("id").asInstanceOf[Attribute[UUID]].value

  /**
    * Get the content of the log.
    *
    * @return
    *   Return the content of the log.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  def getContent: String = attributes("content").asInstanceOf[Attribute[String]].value

  /**
    * Return a view of the object InputSettings.
    *
    * @return
    *   Return a view of the object InputSettings.
    * @since SmartLogger 0.2
    * @version 1.0
    */
  override def toString: String = this.getId.toString + " : " + this.getContent


  // COMMANDES
  /**
    * Sets the value of the log's id
    */
  def setId(uUID: UUID) = {
    attributes("id").asInstanceOf[Attribute[UUID]].set(uUID)
  }

  /**
    * Sets the log's content
    */
  def setContent(text: String) = {
    attributes("content").asInstanceOf[Attribute[String]].set(text)
  }


  // TOOLS
  override protected def initialize(): Map[String, Attribute[_ <: Object]] = {
    val result: Map[String, Attribute[_ <: Object]] = Map.empty
    result.put("id", factory.newUUID(UUID.randomUUID()))
    result.put("content", factory.newString("", 512))
    return result
  }
}
