package fr.saagie.smartlogger.ml

import org.apache.spark.ml.classification.NaiveBayes
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
  * @author Camille LEPLUMEY
  * @since SmartLogger 0.1
  * @version 1.0
  */
@RunWith(classOf[JUnitRunner])
class SmartAnalyzerTest extends FeatureSpec with GivenWhenThen with Matchers {
  feature("This is unitary test for basic behavior of SmartAnalyser") {
    info("more in depth test is done in the benchmark package")

    scenario("Try to execute method train with a null model object.") {
      Given("Instantiate class SmartAnalyzer with NaivesBayes algorithm.")
      val analy = new SmartAnalyzer(new NaiveBayes)
      try {
        When("Train with a null model object")
        analy.train(null)
        Then("An IllegalArgumentException should be thrown")
        fail("The test should throw an IllegalArgumentException")
      } catch {
        case exception: IllegalArgumentException => exception.getMessage should equal("data shouldn't be null")
      }
    }

    scenario("Change the training model before process.") {
      Given("Instantiate class SmartAnalyzer with NaivesBayes algorithm, define a Sequence who contains the new training model and stored the previous model.")
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      val oldModel = analy.model

      When("Training on the new training model.")
      analy.train(seq)

      Then("The model should be updated")
      oldModel should not be analy.model
    }

    scenario("Call predict method on a null object.") {
      Given("Instantiate class SmartAnalyzer with NaivesBayes algorithmn and train with the specific model.")
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
        Then("An exception should rise")
        fail("Prediction not working with null seq at predict")
      } catch {
        case exception: IllegalArgumentException => exception.getMessage should equal("data shouldn't be null")
        case e: Exception => assert(false)
      }
    }

    scenario("Predict same result with the same model") {
      Given("Instantiate class SmartAnalyzer with NaivesBayes algorithmn and train with the specific model.")
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

      When("Train the analyzer with the model.")
      analy.train(seq)

      Then("Prediction should stay the same")
      analy.predict(predi) should equal(seq)
    }
  }
}
