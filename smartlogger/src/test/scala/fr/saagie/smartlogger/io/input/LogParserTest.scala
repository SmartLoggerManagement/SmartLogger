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
class LogParserTest extends FeatureSpec with GivenWhenThen with Matchers {
  feature("LogParser receive log and parse it to return right information.") {
    scenario("Parsing null data.") {
      Given("Instantiate data at null")
      val data = null

      When("LogParser try to parse data.")
      val test = LogParser.parseTrainingData(data)

      Then("The result of the parsing should be at null.")
      test shouldBe null
    }

    scenario("Parse data and see the result.") {
      Given("A parser")
      val data = "1 content .\n2 expected .\n"

      When("We parse a normal content")
      val ret = LogParser.parseTrainingData(data)

      Then("The data parse not empty,")
      ret should not be empty

      And("and the size is equal 2")
      ret.size should equal(2)

      And("and the id of the first element is content ., and 1")
      ret.head._1 should equal("content .")
      ret.head._2 should equal(1)

      And("and the id of the last element is expected ., and 2")
      ret(1)._1 should equal("expected .")
      ret(1)._2 should equal(2)
    }

    scenario("Parse a huge String (1 billion characters)") {
      Given("Initialize the huge string with 1 billion of 'a'")
      val stringSize = 1000000
      val sb = new StringBuilder(stringSize)
      for (i <- 0 until stringSize) {
        sb.append('a')
      }

      When("We parse a big string")
      val ret = LogParser.parseTrainingData("1 " + sb.toString() + " .")

      Then("The return should be correct")
      ret should not be empty

      And("and the size of the Seq is 1")
      ret.size should equal(1)

      And("and the id of the first element is aaaa(1B) ., and 1")
      ret.head._1 should equal(sb.toString() + " .")
      ret.head._2 should equal(1)
    }

    scenario("Parse datab with invalid format.") {
      Given("Instantiate data at null")
      val data = "expected .\n"

      When("LogParser parse log with wrong format.")
      val test = LogParser.parseTrainingData(data)

      Then("The result of the parsing should be empty.")
      test shouldBe empty
    }


    scenario("Parse to without identifier at first.") {
      Given("Initialize data at parse.")
      val data = "content .\nexpected .\n"

      When("Parse data.")
      val ret = LogParser.parsePredictData(data)

      Then("Return should be not empty")
      ret should not be empty

      And("and contains 2 elements")
      ret.size should equal(2)

      And("and the id of the first element is 'content .' and second element is equal to 'expected .'")
      ret.head should equal("content .")
      ret(1) should equal("expected .")
    }
  }
}
