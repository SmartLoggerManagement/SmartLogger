package input

/**
  * Created by Madzinah on 10/02/2017.
  */
object LogParser {

    /**
      * Parse a raw view of log(s) and return
      * a Seq separating the label(s) and the log(s)
      *
      * @param data raw view
      * @return a Seq corresponding to the input, putting each log(s) with
      *         its label
      */

    def parseTrainingData(data: String): Seq[(Long, String, Double)] = {
        if (data == null) {
            return null
        }

        var sequence : Seq[(Long, String, Double)] = Seq.empty
        val logs =  data.split("\n")

        var count = 0L

        for (line <- logs) {
            if (line.contains(" ") && line.matches("^[0-9]* .*$")) {
                var result = line.split(" ", 2)
                sequence = sequence.:+(count, result(1), result(0).toDouble)
                count += 1
            }
        }

        return sequence
    }

  /**
    * Parse a raw view of log(s) and return
    * a Seq separating the ID and the log(s)
    *
    * @param data raw view
    * @return a Seq corresponding to the input, putting each log(s) with
    *         its ID
    */

  def parsePredictData(data: String): Seq[(Long, String)] = {
    if (data == null) {
      return null
    }

    var sequence : Seq[(Long, String)] = Seq.empty
    val logs =  data.split("\n")

    var count = 0L

    for (line <- logs) {
      sequence = sequence.:+(count, line)
      count += 1
    }

    return sequence
  }

}
