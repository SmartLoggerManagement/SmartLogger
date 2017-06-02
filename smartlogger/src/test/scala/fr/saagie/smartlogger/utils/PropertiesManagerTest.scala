package fr.saagie.smartlogger.utils

import java.nio.file.{Files, Paths}

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FeatureSpec, GivenWhenThen}

/**
  *
  * @author Jordan Baudin
  * @since SmartLogger 0.2
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class PropertiesManagerTest extends FeatureSpec with GivenWhenThen {
  feature("A properties manager manages values in a file") {
    scenario("A non existing file can be loaded by creating it") {
      Given("A non existing file and a PropertiesManager")
      val unPresentFile = "src/test/resources/notFound.properties"

      val propertiesManager = new PropertiesManager

      When("The user try to load it")
      propertiesManager.load(unPresentFile)

      Then("The file should exist")
      assert(Files.exists(Paths.get(unPresentFile)))

      Files.deleteIfExists(Paths.get(unPresentFile))
    }

    scenario("An existing file can be loaded and its values used") {
      Given("An existing file")
      val filepath = "src/test/resources/test.properties"
      And("A PropertiesManager")
      val propertiesManager = new PropertiesManager

      When("The file is loaded")
      propertiesManager.load(filepath)

      Then("The properties should not be empty")
      assert(propertiesManager.getProperties().nonEmpty)
      And ("The correct values should be there")
      assert(propertiesManager.get("valueTest1") == "name")
      assert(propertiesManager.get("valueTest2") == "password")
    }

    scenario("Data should be saved correctly") {
      Given("A new file")
      val filepath = "src/test/resources/newFile.properties"
      And("A PropertiesManager with the file loaded")
      val propertiesManager = new PropertiesManager
      propertiesManager.load(filepath)
      And("A set of values")
      propertiesManager.set("data1", "value1")
      propertiesManager.set("testing2", "testingValue2")

      When("The file is saved")
      propertiesManager.save()

      Then("The file should not be empty")
      assert(Files.size(Paths.get(filepath)) != 0)
      And ("The correct values should be there")
      val fileLines = Files.readAllLines(Paths.get(filepath))
      assert(fileLines.get(0) == "data1=value1")
      assert(fileLines.get(1) == "testing2=testingValue2")

      Files.deleteIfExists(Paths.get(filepath))
    }

    scenario("An empty file can be loaded, the data should be empty") {
      Given("An existing file")
      val filepath = "src/test/resources/emptyTest.properties"
      And("A PropertiesManager")
      val propertiesManager = new PropertiesManager

      When("The file is loaded")
      propertiesManager.load(filepath)

      Then("The properties should not be empty")
      assert(propertiesManager.getProperties().isEmpty)
    }
  }
}
