package fr.saagie.smartlogger.io.input

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}

/**
  * Created by teegreg on 06/03/17.
  */
@RunWith(classOf[JUnitRunner])
class LogParserTest extends FunSuite with Matchers {


  test("parseTrainingDataNull") {
    //given
    //when
    val test = LogParser.parseTrainingData(null)
    //then
    test shouldBe null
  }

  test("parsePredictDataNull") {
    //given
    //when
    val test = LogParser.parsePredictData(null)
    //then
    test shouldBe null
  }

  test("Comportement normal parseTrainData") {
    //given
    //when
    val ret = LogParser.parseTrainingData("1 content .\n2 expected .\n")
    //then
    ret should not be empty
    ret.head._1 should equal(0L)
    ret.head._2 should equal("content .")
    ret.head._3 should equal(1)
    ret(1)._1 should equal(1L)
    ret(1)._2 should equal("expected .")
    ret(1)._3 should equal(2)
  }

  test("Comportement limite parseTrainData") {
    //given
    val stringSize = 1000000 //1M
    val sb = new StringBuilder(stringSize)
    for (i <- 0 until stringSize) {
      sb.append('a')
    }
    //when
    val ret = LogParser.parseTrainingData("1 "+ sb.toString() + " .")
    //then
    ret should not be empty
    ret.head._1 should equal(0L)
    ret.head._2 should equal(sb.toString() + " .")
    ret.head._3 should equal(1)
  }

  test("Comportement faux parseTrainData") {
    //given
    //when
    val ret = LogParser.parseTrainingData("expected .\n")
    //then
    ret shouldBe empty
  }


  test("Comportement normal parsePredictData") {
    //given
    //when
    val ret = LogParser.parsePredictData("content .\nexpected .\n")
    //then
    ret should not be empty
    ret.head._1 should equal(0L)
    ret.head._2 should equal("content .")
    ret(1)._1 should equal(1L)
    ret(1)._2 should equal("expected .")
  }

  test("Comportement limite parsePredictData") {
    //given
    val stringSize = 1000000 //1M
    val sb = new StringBuilder(stringSize)
    for (i <- 0 until stringSize) {
      sb.append('a')
    }
    //when
    val ret = LogParser.parsePredictData(sb.toString())
    //then
    ret should not be empty
    ret.head._1 should equal(0L)
    ret.head._2 should equal(sb.toString())

  }

}
