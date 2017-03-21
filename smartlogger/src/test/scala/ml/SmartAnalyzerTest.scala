import org.apache.spark.ml.classification.NaiveBayes
import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SmartAnalyzerTest extends FunSuite with Matchers {

    /* testing train(null)
    * expected behaviour : illegal argument exception
    */
    test("test train with null object") {
      val analy = new SmartIAnalyzer(new NaiveBayes)
        try {
          analy.train(null)
          fail("The test should throw an IllegalArgumentException")
        } catch {
          case exception:IllegalArgumentException => exception.getMessage should equal("data shouldn't be null")
        }
    }

    /*Testing train(seq)
    *expected behavior : old.analy.model != new.analy.model
     */
    test("testTrainChange") {
      val analy = new SmartIAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      val oldModel = analy.model
      analy.train(seq)
      assert(oldModel != analy.model)
    }

    /*Testing predict(null)
    *expected behavior : IllegalArgumentException
     */
    test("testPredictNull") {
      val analy = new SmartIAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      analy.train(seq)
      try {
        analy.predict(null)
        fail("Prediction not working with null seq at predict")
      } catch {
        case exception:IllegalArgumentException => exception.getMessage should equal("data shouldn't be null")
        case e:Exception => assert(false)
      }
    }

    /*Testing predict(seq) giving always the same result
    *expected behavior : predict(seq) == predict(seq)
     */
    test("test Predict Same Result") {
      val analy = new SmartIAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      analy.train(seq)
      val predi = Seq.apply(
        (0L, "X totally sucks :-("),
        (1L, "Today was kind of meh"),
        (2L, "I'm so happy :-)")
      )
      analy.predict(predi) should equal(seq)
    }
}
