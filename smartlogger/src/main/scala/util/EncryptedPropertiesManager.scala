package util

/**
  * Class who extends PropertiesManager.
  *
  * Added data encryption on property file for added security.
  *
  * @author Jordan Baudin
  * @see PropertiesManager
  * @since SmartLogger 0.1
  * @version 1.0
  */
class EncryptedPropertiesManager extends PropertiesManager {


  /**
    * Save the file encoding the values to avoid storing
    * plain text passwords and API keys
    *
    * @since SmartLogger 0.1
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
    * @since SmartLogger 0.1
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
