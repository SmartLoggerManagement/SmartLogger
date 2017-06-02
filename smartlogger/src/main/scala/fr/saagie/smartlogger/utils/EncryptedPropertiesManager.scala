package fr.saagie.smartlogger.utils

/**
  * Defines a specific kind of PropertiesManager, which uses
  * data encryption in order to protect some properties
  * inside the associated .properties file.
  *
  * The "protected" properties are defined by prefixing
  * them with a "$", inside the .properties file
  *
  * @author Jordan Baudin
  * @see PropertiesManager
  * @since SmartLogger 0.2
  * @version 1.0
  */
protected[utils] class EncryptedPropertiesManager extends PropertiesManager {
  // COMMANDS
  /**
    * Save the file encoding the values to avoid storing
    * plain text passwords and API keys
    *
    * @since SmartLogger 0.2
    * @version 1.0
    */
  override def save(): Unit = {
    // Encrypt
    for (k <- getProperties()) {
      if (k.startsWith("$")) {
        set(k, java.util.Base64
          .getEncoder
          .encodeToString(get(k).getBytes))
      }
    }

    super.save()
  }

  /**
    * Load the file decoding the values to avoid storing
    * plain text passwords and API keys
    *
    * @since SmartLogger 0.2
    * @version 1.0
    */
  override def load(filepath: String): PropertiesManager = {
    super.load(filepath)

    // Decrypt
    for (k <- getProperties()) {
      if (k.startsWith("$")) {
        set(k, new String(java.util.Base64
          .getDecoder
          .decode(get(k).getBytes)))
      }
    }

    return this
  }
}
