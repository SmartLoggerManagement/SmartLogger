import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
  * Created by teegreg on 22/03/17.
  */
class DbConnector$Test extends FeatureSpec with Matchers with GivenWhenThen {
  feature("DbConnector connect to a database and return a connection") {

    scenario("testCloseConnection") {
      Given("An open connection")
      val connection = DbConnector.openConnection
      When("We close it")
      DbConnector.closeConnection
      Then("It should be closed")
      connection.isClosed shouldBe true
    }

    scenario("testCloseNonOpenedConection") {
      Given("Nothing")
      When("We close it")
      DbConnector.closeConnection
      Then("It should do nothing")
    }

    scenario("testCloseNonOpenedConection") {
      Given("An open connection")
      val connection = DbConnector.openConnection
      When("We close it twice")
      DbConnector.closeConnection
      DbConnector.closeConnection
      Then("It should be closed")
      connection.isClosed shouldBe true
    }

    scenario("testOpenConnection") {
      Given("Nothing")
      When("We open the connection")
      val connection = DbConnector.openConnection
      connection.isClosed shouldBe false
    }
  }

}
