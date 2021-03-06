package fr.saagie.smartlogger.io.input

/**
  * This object aims at parsing all Logs received from HTTP Requests,
  * which have been read by the InputManager
  *
  * @author Jordan Baudin
  * @since SmartLogger 0.1
  * @version 1.0
  */
object LogParser {
  /**
    * Parse a raw view of log(s) and return
    * a Seq separating the label(s) and the log(s)
    *
    * @param data
    * raw view
    * @return
    * a Seq corresponding to the input, putting each log(s) with
    * its label
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def parseTrainingData(data: String): Seq[(String, Double)] = {
    if (data == null) {
      return null
    }

    var sequence: Seq[(String, Double)] = Seq.empty
    val logs = data.split("\n")

    for (line <- logs) {
      if (line.contains(" ") && line.matches("^[0-9]* .*$")) {
        val result = line.split(" ", 2)
        sequence = sequence.:+(result(1), result(0).toDouble)
      }
    }

    return sequence
  }

  /**
    * Parse a raw view of log(s) and return
    * a Seq separating the ID and the log(s)
    *
    * @param data
    * raw view
    * @return
    * a Seq corresponding to the input, putting each log(s) with
    * its ID
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def parsePredictData(data: String): Seq[String] = {
    if (data == null) {
      return null
    }

    var sequence: Seq[String] = Seq.empty
    val logs = data.split("\n")

    for (line <- logs) {
      sequence = sequence.:+(line)
    }

    return sequence
  }
}
