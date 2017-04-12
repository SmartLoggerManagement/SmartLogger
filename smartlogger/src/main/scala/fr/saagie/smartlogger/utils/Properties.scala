package fr.saagie.smartlogger.utils

/**
  * Defines all kinds of Properties bundles available
  *
  * @author Franck Caron
  * @since SmartLogger 0.2
  * @version 1.0
  */
object Properties {
  // ATTRIBUTES
  /**
    * The directory containing all .properties files
    */
  private val PROPERTIES_DIRECTORY = "src/main/resources/"

  // PROPERTIES MANAGERS
  /**
    * The property manager associated with mail properties
    */
  val MAIL = load("mail.properties")

  /**
    * The property manager associated with slack properties
    */
  val SLACK = load("slack.properties")

  /**
    * The property manager associated with slack properties
    */
  val AKKA = load("akka.properties")

  /**
    * The property manager associated with database properties
    */
  val DB = load("db.properties")

  /**
    * The property manager associated with test database properties
    */
  val DB_TEST = load("dbtest.properties")


  // TOOL
  /**
    * Loads the PropertiesManager associated with the given
    * file, which must be located in PROPERTIES_DIRECTORY
    */
  private def load(filename:String) = new EncryptedPropertiesManager()
    .load(PROPERTIES_DIRECTORY + filename)
}
