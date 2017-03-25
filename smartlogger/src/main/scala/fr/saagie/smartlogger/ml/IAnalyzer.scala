package fr.saagie.smartlogger.ml

import java.rmi.NotBoundException

/**
  * SmartLogger's core, provides all analysis functions which are needed
  * to detect warnings or failure from a given log.
  *
  * The analyzer will be using the log as a triplet of an ID,
  * a text content, and a value which acts as a classification value.
  *
  * @author Jordan Baudin
  * @since SmartLogger 0.1
  * @version 1.0
  */
trait IAnalyzer {
  /**
    * Method used to train Machine Learning.
    *
    * @param data
    *   Seq describing an ID, a Log's content, a Label
    * @throws IllegalArgumentException if data is null
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def train(data: Seq[(Long, String, Double)]): Unit

  /**
    * Method used to predict result.
    *
    * You call this method only if you have called previously the method train.
    *
    * @param data
    *   Seq describing an ID and a Log
    * @return
    *   Seq equals to the input Seq, with a new column which is the prediction
    * @throws IllegalArgumentException if data is null
    * @throws NotBoundException if the model is not created beforehand
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  def predict(data: Seq[(Long, String)]): Seq[(Long, String, Double)]
}
