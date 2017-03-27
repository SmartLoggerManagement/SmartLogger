package fr.saagie.smartlogger.io.input

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FeatureSpec, FunSuite, GivenWhenThen, Matchers}

/**
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class LogBatchTest extends FeatureSpec with GivenWhenThen with Matchers {
  feature("This class tests LogBatch")

  scenario("testGetEmptyBatch") {
    Given("An empty Batch")
    When("get")
     val sq = LogBatch.getBatch()
    Then("The retured sequence should be empty")
    sq.length should equal(0)
  }

  scenario("testAdd") {
    Given("A batch and a sequence")
    var sequence : Seq[(Long, String)] = Seq.empty
    for (i <- 0L until 2L) {
      sequence = sequence.:+(i, "Log" + i)
    }
    When("We add the sequence")
    LogBatch.add(sequence)

    val sq = LogBatch.getBatch()
    Then("This sequence should contain 2 elements")
    sq.length should equal(2)
    for (i <- 0 until 2) {
      sq(i)._1 should equal(i)
      sq(i)._2 should equal("Log" + i)
    }
  }

  scenario("testAdd2") {
    Given("An empty Batch")
    When("We add something")
    LogBatch.add(0L, "Log1")
    val sq = LogBatch.getBatch()
    Then("Batch should return it")
    sq.head._1 should equal(0L)
    sq.head._2 should equal("Log1")
  }

}
