import org.apache.spark.ml.classification.NaiveBayes
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SmartAnalyzerTest extends FunSuite {

    /* testing train(null)
    * expected behaviour : illegal argument exception
    */
    test("testTrainNull") {
      val analy = new SmartAnalyzer(new NaiveBayes)
        try {
          analy.train(null)
          fail("The test should throw an IllegalArgumentException")
        } catch {
          case exception:IllegalArgumentException => assert(true)
          case e:Exception => assert(false)
        }
    }

    /*Testing train(seq) where seq has null
    *expected behavior : illegal argument exception
     */

    test("testTrainNullParam2") {
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, null, 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      try {
        analy.train(seq)
        fail("The test should throw an IllegalArgumentException")
      } catch {
        case exception:IllegalArgumentException => assert(true)
        case e:Exception => assert(false)
      }
    }

    /*Testing train(seq)
    *expected behavior : old.analy.model != new.analy.model
     */
    test("testTrainChange") {
      val analy = new SmartAnalyzer(new NaiveBayes)
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
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      analy.train(seq)
      try {
        analy.predict(null)
        assert(false)
      } catch {
        case exception:IllegalArgumentException => assert(true)
        case e:Exception => assert(false)
      }
    }
  /*Testing predict(seq) where seq has null
  *expected behavior : illegal argument exception
   */

  test("testPredictNullParam2") {
    val analy = new SmartAnalyzer(new NaiveBayes)
    val seq = Seq.apply(
      (0L, "X totally sucks :-(", 0.0),
      (1L, "Today was kind of meh", 1.0),
      (2L, "I'm so happy :-)", 2.0)
    )
    val predi = Seq.apply(
      (4L, null),
      (5L, "i burned my bacon :-("),
      (6L, "the movie is kind of meh")
    )
    analy.train(seq)
    try {
      analy.predict(predi)
      assert(false)
    } catch {
      case exception:IllegalArgumentException => assert(true)
      case e:Exception => assert(false)
    }
  }

    /*Testing predict(seq) giving always the same result
    *expected behavior : predict(seq) == predict(seq)
     */
    test("testPredictSameRes") {
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      val predi = Seq.apply(
        (0L, "X totally sucks :-("),
        (1L, "Today was kind of meh"),
        (2L, "I'm so happy :-)")
      )
      assert(analy.predict(predi) == seq)
    }
}
