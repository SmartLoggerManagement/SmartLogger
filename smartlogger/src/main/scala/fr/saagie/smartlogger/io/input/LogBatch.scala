package fr.saagie.smartlogger.io.input

/**
  * LogBatch is a class used to store Logs received from HTTP request.
  *
  * This batch is used to provide a bunch of logs to the analyser,
  * within a regular time interval.
  *
  * @author Jordan Baudin
  * @since SmartLogger 0.1
  * @version 1.0
  */
object LogBatch {
  // ATTRIBUTES
  /** All stored logs */
  private var sequence: Seq[String] = Seq.empty

  // COMMANDS
  /**
    * Function adding a sequence given to the batch.
    *
    * @param log
    * Sequence of ids and logs
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def add(log: Seq[String]): Unit = {
    sequence = sequence.++(log)
  }

  /**
    * Function adding an id with its log to the batch.
    *
    * @param log
    * log to add to the batch
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def add(log: String): Unit = {
    sequence = sequence.:+(log)
  }

  /**
    * Getter for the batch, returning the reference to the batch
    * and deleting it from the object.
    *
    * @return
    * The Batch
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def getBatch(): Seq[String] = {
    this.synchronized {
      val batch = sequence
      sequence = Seq.empty
      return batch
    }
  }
}
