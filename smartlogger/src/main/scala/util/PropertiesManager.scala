package util

import java.nio.file.{Files, Paths}
import java.util.PropertyResourceBundle

import scala.collection.mutable.Map

/**
  * Class use to manage SmartLogger properties.
  *
  * @author Franck Caron
  * @since SmartLogger 0.1
  * @version 1.0
  */
class PropertiesManager() {
  // Attributes
  /**
    * Name of the property file associate at the manager.
    */
  private var filepath: String = ""

  /**
    * Map containing the key and the associate value.
    */
  private var data: Map[String, String] = Map.empty


  // Requests
  /**
    * Return the value associated to the key.
    *
    * @param property
    * The key.
    * @return
    * The value.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def get(property: String): String = data(property)

  /**
    * Return all properties present on property files.
    *
    * @return
    * A sequence who contains all properties.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def getProperties(): Seq[String] = data.keys.toSeq

  /**
    * Check if a property exists on property file.
    *
    * @return
    * True if the property exists, in other case, it return false.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def exists(property: String): Boolean = getProperties().contains(property)


  // Commands
  /**
    * Loading all properties present on specific file.
    *
    * @param filepath
    * The complete path of the property file.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def load(filepath: String): PropertiesManager = {
    // Reinitialisation
    this.filepath = filepath
    this.data = Map.empty

    if (!Files.exists(Paths.get(filepath))) {
      Files.createFile(Paths.get(filepath))
      return this
    }

    // Loading properties
    val bundle = new PropertyResourceBundle(Files.newBufferedReader(Paths.get(filepath)))

    val iterator = bundle.keySet()
      .iterator()

    while (iterator.hasNext) {
      val key = iterator.next
      data update(key, bundle.getString(key))
    }

    return this
  }

  /**
    * Set the value in function of the key.
    *
    * @param property
    * Property name.
    * @param value
    * Associated value.
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def set(property: String, value: String): Unit = data(property) = value

  /**
    * Save the file with new properties.
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def save(): Unit = {
    // Open destination file.
    val writer = Files.newBufferedWriter(Paths.get(filepath))

    // Overwrite file.
    for (key: String <- data.keys) {
      writer.write(key + "=" + data(key))
      writer.newLine()
    }

    // Close file.
    writer.close()
  }
}
