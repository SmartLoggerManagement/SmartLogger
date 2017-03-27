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
class LogParserTest extends FeatureSpec with GivenWhenThen with Matchers {


  scenario("parseTrainingDataNull") {
    Given("An empty parser")
    When("We parse null data")
    val test = LogParser.parseTrainingData(null)
    Then("test should be null")
    test shouldBe null
  }

  scenario("parsePredictDataNull") {
    Given("A parser")
    When("We predict Null data")
    val test = LogParser.parsePredictData(null)
    Then("test shouldBe null")
    test shouldBe null
  }

  scenario("Comportement normal parseTrainData") {
    Given("A parser")
    When("We parse a normal content")
    val ret = LogParser.parseTrainingData("1 content .\n2 expected .\n")
    Then("The return should be correct")
    ret should not be empty
    ret.head._1 should equal(0L)
    ret.head._2 should equal("content .")
    ret.head._3 should equal(1)
    ret(1)._1 should equal(1L)
    ret(1)._2 should equal("expected .")
    ret(1)._3 should equal(2)
  }

  scenario("Comportement limite parseTrainData") {
    Given("A parser")
    val stringSize = 1000000 //1M
    val sb = new StringBuilder(stringSize)
    for (i <- 0 until stringSize) {
      sb.append('a')
    }
    When("We parse a big string")
    val ret = LogParser.parseTrainingData("1 "+ sb.toString() + " .")
    Then("The return should be correct")
    ret should not be empty
    ret.head._1 should equal(0L)
    ret.head._2 should equal(sb.toString() + " .")
    ret.head._3 should equal(1)
  }

  scenario("Comportement faux parseTrainData") {
    Given("A parser")
    When("We parse a wrong log")
    val ret = LogParser.parseTrainingData("expected .\n")
    Then("Return should be empty")
    ret shouldBe empty
  }


  scenario("Comportement normal parsePredictData") {
    Given("A parser")
    When("We parse expectable data")
    val ret = LogParser.parsePredictData("content .\nexpected .\n")
    Then("Return should be normal")
    ret should not be empty
    ret.head._1 should equal(0L)
    ret.head._2 should equal("content .")
    ret(1)._1 should equal(1L)
    ret(1)._2 should equal("expected .")
  }

  scenario("Comportement limite parsePredictData") {
    Given("A parser")
    val stringSize = 1000000 //1M
    val sb = new StringBuilder(stringSize)
    for (i <- 0 until stringSize) {
      sb.append('a')
    }
    When("")
    val ret = LogParser.parsePredictData(sb.toString())
    Then("")
    ret should not be empty
    ret.head._1 should equal(0L)
    ret.head._2 should equal(sb.toString())

  }

}
