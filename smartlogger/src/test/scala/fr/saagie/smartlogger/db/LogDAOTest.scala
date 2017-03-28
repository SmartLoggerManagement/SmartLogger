package fr.saagie.smartlogger.db

import java.util.UUID

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FeatureSpec, GivenWhenThen, Matchers}

/**
  * @author Camille LEPLUMEY
  * @since SmartLogger 0.2
  * @version 1.0
  */

@RunWith(classOf[JUnitRunner])
class LogDAOTest extends FeatureSpec with Matchers with GivenWhenThen with BeforeAndAfter {
  feature("The Database executes the query and fills the sequence 'result' with the resulting logs.") {
    info("To simulate the database's behaviour, we will mock its responses.")

    before() {
      val uuid = UUID.randomUUID()
    }

    scenario("A create log query is sent to the database.") {
      Given("Create the 'set' query with the message")
      val message = "I am correct log"
      val insertQuery: String = "Insert into Logs values (" + uuid + ", " + message + ")"

      When("Query the Database with the query")
      LogDAO.execute(insertQuery)

      Then("Check that the query passed successfully")
      assert(true)
    }

    scenario("A get query is sent to the database. The database responds with the appropriate answer.") {
      Given("Create the string 'get' query.")
      val query: String = "Select * from Logs where id = " + uuid + ")"

      When("Query the Database with the query and analyse the result")
      val result = LogDAO.query(query)

      Then("The log id must be equal to uuid")
      result.head.getId.toString() should equal(uuid.toString())

      And("the message must be equal to 'I am correct Log'")
      result.head.getLog.toString() should equal("I am correct Log")
    }

    scenario("A get query is sent to the database with an invalid uuid. The database responds with the appropriate answer.") {
      Given("Create the invalid uuid and the 'get' query")
      val invalidUuid = UUID.randomUUID()
      val query: String = "Select * from Logs where id = " + invalidUuid + ")"

      When("Query the Database with the query and analyse the result")
      val result = LogDAO.query(query)

      Then("The log id must launch an exception")
      intercept[Exception] {
        result.head.getId
      }

      And("The log message must launch an exception")
      intercept[Exception] {
        result.head.getLog
      }
    }
  }
}
