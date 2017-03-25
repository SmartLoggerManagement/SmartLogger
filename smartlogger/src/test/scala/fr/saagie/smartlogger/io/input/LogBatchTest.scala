package fr.saagie.smartlogger.io.input

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}

/**
  * Created by teegreg on 03/03/17.
  */
@RunWith(classOf[JUnitRunner])
class LogBatchTest extends FunSuite with Matchers {

  test("testGetEmptyBatch") {
  val sq = LogBatch.getBatch()
  sq.length should equal(0)
  }

  test("testAdd") {
    var sequence : Seq[(Long, String)] = Seq.empty
    for (i <- 0L until 2L) {
      sequence = sequence.:+(i, "Log" + i)
    }
    LogBatch.add(sequence)

    val sq = LogBatch.getBatch()
    sq.length should equal(2)
    for (i <- 0 until 2) {
      sq(i)._1 should equal(i)
      sq(i)._2 should equal("Log" + i)
    }
  }

  test("testAdd2") {
    LogBatch.add(0L, "Log1")
    val sq = LogBatch.getBatch()
    sq.head._1 should equal(0L)
    sq.head._2 should equal("Log1")
  }

}
