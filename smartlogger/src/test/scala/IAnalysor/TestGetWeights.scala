package scala.IAnalysor

import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

import scala.ML.StubAnalyzer

@RunWith(classOf[JUnitRunner])
class TestGetWeights extends FlatSpec with Matchers {
  private val analyzer = new StubAnalyzer

  def testNull() {
    assert(analyzer.weights == null)
  }

  def testSentType() {
    assert(analyzer.getWeights.isInstanceOf[String])
  }
}
