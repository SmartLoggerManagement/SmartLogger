package fr.saagie.smartlogger.db

import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
  * @author Grégoire POMMIER
  * @since SmartLogger 0.2
  * @version 1.0
  */
class DbConnectorTest extends FeatureSpec with Matchers with GivenWhenThen {
  feature("DbConnector connect to a database and return a connection") {
    scenario("Test the close connection method.") {
      Given("An open connection")
      val connection = DbConnector.openConnection

      When("We close it")
      DbConnector.closeConnection

      Then("It should be closed")
      connection.isClosed shouldBe true
    }

    scenario("The connection is closed and the user try to close at new.") {
      Given("An open connection")
      val connection = DbConnector.openConnection
      DbConnector.closeConnection

      When("We close it")
      DbConnector.closeConnection

      Then("It should do nothing")
      connection.isClosed shouldBe true
    }

    scenario("The connection is close and the user try to close at new.") {
      Given("An open connection")
      val connection = DbConnector.openConnection
      DbConnector.closeConnection

      When("We close it twice")
      DbConnector.closeConnection

      Then("It should be closed")
      connection.isClosed shouldBe true
    }

    scenario("The DbConnector open a connection from the Database.") {
      Given("Nothing")
      When("We open the connection")
      val connection = DbConnector.openConnection

      Then("The connection should be closed")
      connection.isClosed shouldBe false
    }
  }
}
