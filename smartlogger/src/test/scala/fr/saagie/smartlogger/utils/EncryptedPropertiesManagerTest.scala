package fr.saagie.smartlogger.utils

import java.nio.file.{Files, Paths}

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
  *
  * @author Gregoire Pommier
  * @since SmartLogger 0.2
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class EncryptedPropertiesManagerTest extends FeatureSpec with Matchers with GivenWhenThen {
  feature("The feature tested is a class EncryptedPropertiesManager used to encrypt properties.") {
    scenario("Data should be saved correctly") {
      Given("A new file")
      val filepath = "src/test/resources/testencrypted.properties"
      And("A PropertiesManager with the file loaded")
      val propertiesManager = new EncryptedPropertiesManager
      propertiesManager.load(filepath)
      And("A set of values")
      propertiesManager.set("data1", "value1")
      propertiesManager.set("$testing2", "testingValue2")

      When("The file is saved")
      propertiesManager.save()

      Then("The file should not be empty")
      assert(Files.size(Paths.get(filepath)) != 0)
      And ("The correct values should be there")
      val fileLines = Files.readAllLines(Paths.get(filepath))
      assert(fileLines.get(1) == "data1=value1")
      assert(fileLines.get(0) != "$testing2=testingValue2")

      Files.deleteIfExists(Paths.get(filepath))
    }

    scenario("Data should be loaded correctly") {
      Given("A new file")
      val filepath = "src/test/resources/testencrypted.properties"
      And("A PropertiesManager with the file loaded")
      val propertiesManager = new EncryptedPropertiesManager
      propertiesManager.load(filepath)
      And("A set of values")
      propertiesManager.set("valueTest1", "name")
      propertiesManager.set("$valueTest2", "password")
      And("Saved")
      propertiesManager.save()

      When("The file is loaded")
      propertiesManager.load(filepath)

      Then("Values should be correct")
      assert(propertiesManager.get("valueTest1") == "name")
      assert(propertiesManager.get("$valueTest2") == "password")

      Files.deleteIfExists(Paths.get(filepath))
    }

    scenario("An empty file can be loaded, the data should be empty") {
      Given("An existing file")
      val filepath = "src/test/resources/emptyTest.properties"
      And("A PropertiesManager with the file loaded")
      val propertiesManager = new EncryptedPropertiesManager

      When("The file is loaded")
      propertiesManager.load(filepath)

      Then("The properties should be empty")
      assert(propertiesManager.getProperties().isEmpty)
    }
  }
}


