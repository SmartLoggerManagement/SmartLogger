package fr.saagie.smartlogger.db.model

import java.util.UUID

import org.junit.runner.RunWith
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}
import org.scalatest.junit.JUnitRunner

/**
  * @author Nicolas GILLE
  * @since SmartLogger 0.2
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class LogTest extends FeatureSpec with Matchers with GivenWhenThen {
  feature("The Database information fill an object Log") {
    info("The object DAO request the Database to retrieve information")
    info("So, the DAO fill an object Log with the id and the log")

    scenario("The DAO fill an object Log with precise Log") {
      Given("Create an Id and a Message to simulate information retrieve by the DAO.")
      val id : UUID = UUID.randomUUID()
      val message : String = "My name is Log, James Log"

      When("Fill the Log with id and message previously retrieve.")
      val log = new Log(id, message)

      Then("The log id must be equal to 123456789")
      log.getId should equal(id)

      And("the message must equal at 'My name is Log, James Log'")
      log.getLog should equal(message)
    }
  }
}
