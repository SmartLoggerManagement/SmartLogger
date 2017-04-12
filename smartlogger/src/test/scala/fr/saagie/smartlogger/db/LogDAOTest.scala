package fr.saagie.smartlogger.db

import java.util.UUID

import fr.saagie.smartlogger.db.model.Log
import fr.saagie.smartlogger.db.model.attributes.Attribute
import fr.saagie.smartlogger.db.pgsql.{AttrPGSQLFactory, LogDAO}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FeatureSpec, GivenWhenThen, Matchers}

import scala.collection.mutable.Map

/**
  * @author Camille LEPLUMEY
  * @since SmartLogger 0.2
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class LogDAOTest extends FeatureSpec with Matchers with GivenWhenThen with BeforeAndAfter {
  feature("The Database executes the query and fills the sequence 'result' with the resulting logs.") {
    scenario("Table building request is sent to the database, when no table is already built") {
      Given("Calling DAO to build Table on Database")
      When("Launch of the building process")
      LogDAO.build()
    }

    scenario("Log insertion into the database's table") {
      Given("Create the 'set' query with the message")
      val log = new Log(AttrPGSQLFactory)
      log.setContent("I am correct log")
      log.setId(UUID.randomUUID())

      When("Query the Database with the insert query")
      LogDAO.insert(log)
    }

    scenario("A get query is sent to the database. The database responds with the appropriate answer.") {
      Given("Retrieve information from persistence system.")

      When("Query the Database with the query and analyse the result")
      val result = LogDAO.get()

      Then("The return must contain at least one log")
      result should not be empty
    }

    scenario("A get query is sent to the database with an invalid uuid. The database responds with the appropriate answer.") {
      Given("Create the invalid uuid and the 'get' query")
      val invalidUuid = UUID.randomUUID()

      When("Query the Database with the query and analyse the result")
      val map: Map[String, Attribute[_ <: Object]] = Map.empty
        map.put("id", AttrPGSQLFactory.newUUID(invalidUuid))

      val result = LogDAO.get("id = ?", map)

      Then("The result must have no rows")
      result shouldBe empty
    }

    scenario("Dropping DAO table for test purposes") {
      Given("Create 'DROP' query")
      When("Querying the Database")
      LogDAO.drop()
    }
  }
}
