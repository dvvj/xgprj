package org.xg.db.api

import java.sql.Connection
import java.time.LocalDateTime

import org.joda.time.DateTime

trait TDbOps {
  def addNewCustomer(uid:String, name:String, pass:String, idCardNo:String, mobile:String, postalAddr:String, ref_uid: String, bday:String):Boolean

  def allCustomers(conn:Connection):String
}
