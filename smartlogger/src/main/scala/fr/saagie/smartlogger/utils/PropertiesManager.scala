package fr.saagie.smartlogger.utils

import java.nio.file.{Files, Paths}
import java.util.PropertyResourceBundle

import scala.collection.mutable.Map

/**
  * Implements a basic PropertiesManager.
  *
  * @author Franck Caron
  * @since SmartLogger 0.2
  * @version 1.0
  */
protected[utils] class PropertiesManager {
  // ATTRIBUTES
  /**
    * Name of the property file associate at the manager.
    */
  private var filepath: String = ""

  /**
    * Map containing all (key, value) pairs.
    */
  private var data: Map[String, String] = Map.empty


  // REQUESTS
  def get(property: String): String = data(property)

  def getProperties(): Seq[String] = data.keys.toSeq

  def exists(property: String): Boolean = getProperties().contains(property)


  // Commands
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

  def set(property: String, value: String): Unit = data(property) = value

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
