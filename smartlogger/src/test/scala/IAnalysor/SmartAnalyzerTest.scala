import org.scalatest.FunSuite

class SmartAnalyzerTest extends FunSuite {

    /* testing train(null)
    * expected behaviour : illegal argument exception
    */
    test("testTrainNull") {
      val analy = new SmartAnalyzer(new NaiveBayes)
        try {
          analy.train(null)
          assert(false)
        } catch IllegalArgumentException Exception {
          assert(true)
        }
    }

    /*Testing train(seq) where seq has null
    *expected behavior : illegal argument exception
     */
    test("testTrainNullParam1") {
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (null, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      try {
        analy.train(seq)
        assert(false)
      } catch IllegalArgumentException Exception {
        assert(true)
      }
    }

    test("testTrainNullParam2") {
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, null, 1.0),
        (2L, "I'm so happy :-)", 2.0)
      )
      try {
        analy.train(seq)
        assert(false)
      } catch IllegalArgumentException Exception {
        assert(true)
      }
    }

    test("testTrainNullParam3") {
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (0L, "X totally sucks :-(", 0.0),
        (1L, "Today was kind of meh", 1.0),
        (2L, "I'm so happy :-)", null)
      )
      try {
        analy.test(seq)
        assert(false)
      } catch IllegalArgumentException Exception {
        assert(true)
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
      try {
        analy.predict(null)
        assert(false)
      } catch IllegalArgumentException Exception {
        assert(true)
      }
    }
  /*Testing predict(seq) where seq has null
  *expected behavior : illegal argument exception
   */
  test("testPredictNullParam1") {
    val analy = new SmartAnalyzer(new NaiveBayes)
    val seq = Seq.apply(
      (null, "X totally sucks :-(", 0.0),
      (1L, "Today was kind of meh", 1.0),
      (2L, "I'm so happy :-)", 2.0)
    )
    try {
      analy.predict(seq)
      assert(false)
    } catch IllegalArgumentException Exception {
      assert(true)
    }
  }

  test("testPredictNullParam2") {
    val analy = new SmartAnalyzer(new NaiveBayes)
    val seq = Seq.apply(
      (0L, "X totally sucks :-(", 0.0),
      (1L, null, 1.0),
      (2L, "I'm so happy :-)", 2.0)
    )
    try {
      analy.predict(seq)
      assert(false)
    } catch IllegalArgumentException Exception {
      assert(true)
    }
  }

  test("testPredictNullParam3") {
    val analy = new SmartAnalyzer(new NaiveBayes)
    val seq = Seq.apply(
      (0L, "X totally sucks :-(", 0.0),
      (1L, "Today was kind of meh", 1.0),
      (2L, "I'm so happy :-)", null)
    )
    try {
      analy.predict(seq)
      assert(false)
    } catch IllegalArgumentException Exception {
      assert(true)
    }
  }

    /*Testing predict(seq) giving always the same result
    *expected behavior : predict(seq) == predict(seq)
     */
    test("testPredictSameRes") {
      val analy = new SmartAnalyzer(new NaiveBayes)
      val seq = Seq.apply(
        (4L, "roller coasters are fun :-)"),
        (5L, "i burned my bacon :-("),
        (6L, "the movie is kind of meh")
      )
      assert(analy.predict(seq) == analy.predict(seq))
    }
}
