package dao

import database.dao.Log

/**
  * Created by Kero76 on 07/03/17.
  *
  * @author Nicolas GILLE
  * @since SmartLogger 0.2
  * @version 1.0
  */
trait DAO {
  def request(request: String): Seq[(Log)]

  def insert(request: String): Boolean
}
