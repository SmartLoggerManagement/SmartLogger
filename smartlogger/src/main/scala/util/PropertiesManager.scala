package util

import java.io.{BufferedWriter, FileReader, FileWriter}
import java.util.PropertyResourceBundle

import scala.collection.mutable.Map

/**
  * Created by franck on 15/03/17.
  */
class PropertiesManager() {
  // ATTRIBUTS
  /** Le nom de fichier properties associé à ce gestionnaire */
  private var filepath:String = ""

  /** La map des modifications à apporter au fichier de propriétés */
  private var data:Map[String, String] = Map.empty


  // REQUETES
  /**
    * Renvoie la valeur textuelle associée au nom de propriété donné
    *
    * @param property Le nom de la propriété
    */
  def get(property:String) : String = data(property)

  /**
    * Renvoie l'ensembles des propriétés définies par le fichier
    * de propriétés spécifié
    */
  def getProperties() : Seq[String] = data.keys.toSeq

  /**
    * Indique si la propriété donnée existe dans le fichier
    * de propriétés
    */
  def exists(property:String) : Boolean = getProperties().contains(property)


  // COMMANDES
  /**
    * Charge toutes les propriétés du fichier spécifié
    */
  def load(filepath:String) : PropertiesManager = {
    // Réinitialisation
    this.filepath = filepath
    this.data = Map.empty

    // Chargement des propriétés
    val bundle = new PropertyResourceBundle(new FileReader(filepath))
    while (bundle.getKeys.hasMoreElements) {
      val key:String = bundle.getKeys.nextElement()
      data += (key -> bundle.getString(key))
    }

    this
  }

  /**
    * Modifie la valeur textuelle associée au nom de propriété donné
    *
    * @param property Le nom de la propriété
    * @param value La valeur à associer à cette propriété
    */
  def set(property:String, value:String) : Unit = data(property) = value

  /**
    * Modifie le fichier de propriétés pour qu'il corresponde aux informations
    * associées à ce gestionnaire
    */
  def save():Unit = {
    // Ouverture du fichier de destination
    val writer = new BufferedWriter(new FileWriter(filepath))

    // Réecriture du fichier
    for (key:String <- data.keys) {
      writer.write(key + "=" + data(key))
      writer.newLine()
    }

    // Fermeture du fichier
    writer.close()
  }
}
