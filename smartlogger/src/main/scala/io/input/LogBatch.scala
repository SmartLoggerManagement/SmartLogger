package input

/**
  * Created by Jordan Baudin on 01/03/17.
  */
object LogBatch {


  var sequence:Seq[(Long, String)] = Seq.empty

  /**
    * Function adding a sequence given to the batch.
    * @param log Sequence of ids and logs
    */
  def add(log: Seq[(Long, String)]):Unit = {
    sequence = sequence.++(log)
  }

  /**
    * Function adding an id with its log to the batch.
    * @param id id corresponding to the log
    * @param log log to add to the batch
    */
  def add(id: Long, log: String):Unit = {
    sequence = sequence.:+((id, log))
  }

  /**
    * Getter for the batch, returning the reference to the batch
    *   and deleting it from the object.
    * @return The Batch
    */
  def getBatch():Seq[(Long, String)] = {
    val batch = sequence
    sequence = Seq.empty
    return batch
  }

}
