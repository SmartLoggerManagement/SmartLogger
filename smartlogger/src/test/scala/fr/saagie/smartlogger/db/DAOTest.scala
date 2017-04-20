package fr.saagie.smartlogger.db

import java.util.UUID

import fr.saagie.smartlogger.db.model.Log
import fr.saagie.smartlogger.db.model.attributes.Attribute
import fr.saagie.smartlogger.db.mysql.MySQLDAOBuilder
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FeatureSpec, GivenWhenThen, Matchers}

import scala.collection.mutable.Map

/**
  * @author Franck CARON, Camille LEPLUMEY
  * @since SmartLogger 0.2
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class DAOTest extends FeatureSpec with Matchers with GivenWhenThen with BeforeAndAfter {
  // VALUES
  /** The builder used for DAO generation */
  val DAOBuilder = MySQLDAOBuilder

  /** A fixed UUID for test operation */
  val defaultId = UUID.randomUUID()

  // FEATURES
  feature("The Database executes the query and fills the sequence 'result' with the resulting logs.") {
    scenario("No table is defined") {
      Given("Getting DAO")
      val dao = DAOBuilder.getLogDAO()

      When("Testing the table's presence")
      val exists = dao.exists()

      Then("Table should not exist")
      exists shouldBe false
    }

    scenario("Table building request is sent to the database, when no table is already built") {
      Given("Calling DAO to build Table on Database")
      val dao = DAOBuilder.getLogDAO()

      When("Launch of the building process")
      dao.build()

      Then("Table should exist")
      dao.exists() shouldBe true
    }

    scenario("Log insertion into the database's table") {
      Given("Create the 'set' query with the message")
      val dao = DAOBuilder.getLogDAO()
      val log = new Log(dao.getAttributeFactory())
      log.setContent("I am correct log")
      log.setId(defaultId)

      When("Query the Database with the insert query")
      dao.insert(log)
    }

    scenario("A get query is sent to the database. The database responds with the appropriate answer.") {
      Given("Retrieve information from persistence system.")
      val dao = DAOBuilder.getLogDAO()

      When("Query the Database with the query and analyse the result")
      val result = dao.get()

      Then("The return must contain at least one log")
      result should not be empty
    }

    scenario("A get query is sent to the database with an invalid uuid. The database responds with the appropriate answer.") {
      Given("Create the invalid uuid and the 'get' query")
      val invalidUuid = UUID.randomUUID()
      val dao = DAOBuilder.getLogDAO()

      When("Query the Database with the query and analyse the result")
      var seq: Seq[Attribute[_ <: Object]] = Seq.empty
        seq = seq.+:(dao.getAttributeFactory().newUUID(invalidUuid))

      val result = dao.get("id = ?", seq)

      Then("The result must have no rows")
      result shouldBe empty
    }

    scenario("Using UPDATE : update of an attribute of a previously inserted log") {
      Given("Initializing values")
      // Initialization
      val dao = DAOBuilder.getLogDAO()
      val newContent = "Updated content"

      // Defining SET part
      val set:Map[String, Attribute[_ <: Object]] = Map.empty
        set.put("content", dao.getAttributeFactory().newString(newContent, 16))
      val where = Seq.empty.+:(dao.getAttributeFactory().newUUID(defaultId))

      When("Making update")
      dao.update(set , "id = ?", where)

      Then("The updated log must have the same content")
      val log = dao.get().head
      assert(log.getContent.equals(newContent))
    }

    scenario("Dropping DAO table for test purposes") {
      Given("Create 'DROP' query")
      val dao = DAOBuilder.getLogDAO()

      When("Querying the Database")
      dao.drop()
    }
  }
}
