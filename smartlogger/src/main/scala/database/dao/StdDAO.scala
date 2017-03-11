package dao

import database.dao.Log

/**
  * Created by Kero76 on 07/03/17.
  *
  * @author Nicolas GILLE
  * @since SmartLogger 0.2
  * @version 1.0
  */
object StdDAO extends DAO {
  override def request(request: String): Seq[(Log)] = {
    return Seq.empty
  }

  override def insert(request: String): Boolean = {
    return true
  }
}
