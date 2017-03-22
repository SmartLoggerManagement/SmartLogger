import java.sql.ResultSet
import java.util
import java.util.UUID

import database.model.Log
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

import scala.collection.immutable.HashSet

/**
  * @author Camille LEPLUMEY
  * @since SmartLogger 0.2
  * @version 1.0
  */

@RunWith(classOf[JUnitRunner])
class LogDAOTest extends FeatureSpec with Matchers with GivenWhenThen {
  feature("The Database executes the query and fills the sequence 'result' with the resulting logs.") {
    info("To simulate the database's behaviour, we will mock its responses.")

    scenario("A querry is sent to the database. The database responds with the appropriate answer.") {
      Given("Create a string querry.")
      val uuid = UUID.randomUUID();
      val query : String = "SELECT Log from Logs where id =" + uuid.toString()

      val resultSetMock = MockitoSugar.mock[ResultSet]
      Mockito.when(resultSetMock.getString("id")).thenReturn(uuid.toString())
      Mockito.when(resultSetMock.getString("log")).thenReturn("I am correct Log")

      val DBMock = MockitoSugar.mock[DbConnector.type]
      Mockito.when(DBMock.openConnection.createStatement.executeQuery("SELECT * from Logs where id = " + uuid.toString())).
        thenReturn(resultSetMock)

      When("Querry the Database with the querry and analyse the result")
      val result = LogDAO.query(query)

      Then("The log id must be equal to uuid")
      result.head.getId.toString() should equal(uuid.toString())

      And("the message must be equal to 'I am correct Log'")
      result.head.getLog.toString() should equal("I am correct Log")
    }
  }
}
