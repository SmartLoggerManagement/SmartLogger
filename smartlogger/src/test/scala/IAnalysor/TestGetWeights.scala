package scala.IAnalysor

import scala.ML.StubAnalyzer

/**
  * Created by Kero76 on 31/01/17.
  */
class TestGetWeights {
  private val analyzer = new StubAnalyzer

  def testNull() {
    assertFalse(analyzer.weights == null)
  }

  def testSentType() {
    assertTrue(true, analyzer.getWeights.isInstanceOf[Seq[Any]])
  }
}
