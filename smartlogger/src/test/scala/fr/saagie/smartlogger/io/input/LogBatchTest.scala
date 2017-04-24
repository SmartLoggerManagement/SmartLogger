package fr.saagie.smartlogger.io.input

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class LogBatchTest extends FeatureSpec with GivenWhenThen with Matchers {
  feature("This class tests LogBatch") {
    scenario("testGetEmptyBatch") {
      Given("An empty Batch")
      When("Retrieve the LogBatch content")
      val sq = LogBatch.getBatch()

      Then("The sequence return should be empty")
      sq.length should equal(0)
    }

    scenario("Add 1 log on LogBatch") {
      Given("Add a Log on LogBatch")
      LogBatch.add("Log1")

      When("Retrieve LogBatch content")
      val sq = LogBatch.getBatch()

      Then("Batch should return 'Log1'")
      sq.head should equal("Log1")
    }

    scenario("Add 2 Logs on LogBatch") {
      Given("Instantiate a Seq with some log.")
      var sequence: Seq[String] = Seq.empty
      for (i <- 0L until 2L) {
        sequence = sequence.:+("Log" + i)
      }
      LogBatch.add(sequence)

      When("Retrieve content of LogBatch")
      val sq = LogBatch.getBatch()

      Then("This sequence should contain 2 elements")
      sq.length should equal(2)
      And("and the result is equal to i and 'Log' + i")
      for (i <- 0 until 2) {
        sq(i) should equal("Log" + i)
      }
    }
  }
}
