package input

/**
  * Created by Jordan Baudin on 01/03/17.
  */
object LogBatch {

  var sequence:Seq[(Long, String)] = Seq.empty

  def add(log: Seq[(Long, String)]):Unit = {
    sequence = sequence.++(log)
  }

  def getBatch():Seq[(Long, String)] = {
    val batch = sequence
    sequence = Seq.empty
    return batch
  }

}
