package org.xg.db.api

import java.time.LocalDateTime

import org.joda.time.DateTime

trait TDbOps {
  def addNewCustomer(uid:String, name:String, idCardNo:String, mobile:String, postalAddr:String, bday:String):Boolean
}
