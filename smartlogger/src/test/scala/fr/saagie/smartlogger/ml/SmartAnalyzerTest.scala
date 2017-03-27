import fr.saagie.smartlogger.ml.SmartAnalyzer
import org.apache.spark.ml.classification.NaiveBayes
import org.junit.runner.RunWith
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}
import org.scalatest.junit.JUnitRunner

/**
  * @author Camille LEPLUMEY
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class SmartAnalyzerTest extends FeatureSpec with GivenWhenThen with Matchers {
    feature("This is unitary test for basic behavior of SmartAnalyser")
      info("more in depth test is done in the benchmark package")

  /* testing train(null)
    * expected behaviour : illegal argument exception
    */
    scenario("test train with null object") {
      Given("A smart analyzer")
      val analy = new SmartAnalyzer(new NaiveBayes)
        try {
          When("Train with a null object")
          analy.train(null)
          Then("An IllegalArgumentExeption should be thrown")
          fail("The test should throw an IllegalArgumentException")
        } catch {
          case exception:IllegalArgumentException => exception.getMessage should equal("data shouldn't be null")
        }
    }

    /*Testing train(seq)
    *expected behavior : old.analy.model != new.analy.model
     */

    scenario("testTrainChange") {
      Given("A smart analyzer")
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      val oldModel = analy.model
      When("Training")
      analy.train(seq)
      Then("The model should be updated")
      oldModel should not be analy.model
    }

    /*Testing predict(null)
    *expected behavior : IllegalArgumentException
     */
    scenario("testPredictNull") {
      Given("A smart analyzer")
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      analy.train(seq)
      try {
        When("Predict with a null object")
        analy.predict(null)
        Then("An exeption should rise")
        fail("Prediction not working with null seq at predict")
      } catch {
        case exception:IllegalArgumentException => exception.getMessage should equal("data shouldn't be null")
        case e:Exception => assert(false)
      }
    }

    /*Testing predict(seq) giving always the same result
    *expected behavior : predict(seq) == predict(seq)
     */
    scenario("test Predict Same Result") {
      Given("A smart analyzer")
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      When("We train")
      analy.train(seq)
      val predi = Seq.apply(
        (0L, "X totally sucks :-("),
        (1L, "Today was kind of meh"),
        (2L, "I'm so happy :-)")
      )
      Then("Prediction should stay the same")
      analy.predict(predi) should equal(seq)
    }
}
