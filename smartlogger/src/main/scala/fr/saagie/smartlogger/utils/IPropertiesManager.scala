package fr.saagie.smartlogger.utils

/**
  * A PropertiesManager allows to store String-attributes
  * into .properties files in order to externalize the configuration
  * of any core module.
  *
  * @author Franck Caron
  * @since SmartLogger 0.2
  * @version 1.0
  */
trait IPropertiesManager {
  // REQUESTS
  /**
    * Return the value associated to the given key.
    *
    * @param property
    * The key.
    * @return
    * The value.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def get(property: String): String

  /**
    * Return all properties handled by this manager.
    *
    * @return
    * A sequence who contains all properties.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def getProperties(): Seq[String]

  /**
    * Check if a property is managed by this manager.
    *
    * @return
    * True if the property exists, in other case, it return false.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def exists(property: String): Boolean


  // COMMANDS
  /**
    * Load all properties from a given .properties file.
    *
    * @param filepath
    * The complete path of the property file.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def load(filepath: String): PropertiesManager

  /**
    * Set/Update the value associated to the given key.
    *
    * @param property
    * Property name.
    * @param value
    * Associated value.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def set(property: String, value: String): Unit

  /**
    * Save the current value of all properties into
    * the .properties file
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def save(): Unit
}