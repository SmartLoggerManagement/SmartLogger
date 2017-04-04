package fr.saagie.smartlogger.utils

import java.nio.file.{Files, Paths}

import fr.saagie.smartlogger.utils.EncryptedPropertiesManager
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FeatureSpec, FunSuite, GivenWhenThen, Matchers}

/**
  *
  * @author Gregoire Pommier
  * @since SmartLogger 0.2
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class EncryptedPropertiesManagerTest extends FeatureSpec with Matchers with GivenWhenThen {

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
}


