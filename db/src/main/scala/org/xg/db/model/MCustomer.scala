package org.xg.db.model

import org.json4s.DefaultFormats

case class MCustomer(
  uid:String,
  name:String,
  idCardNo:String,
  mobile:String,
  postalAddr:String,
  bday:String,
  refUid:String
)

object MCustomer {
  import org.xg.json.CommonUtils._
  def toJsons(customers:Array[MCustomer]):String = _toJsons(customers)
  def fromJsons(j:String):Array[MCustomer] = _fromJsons(j)
  //
//  def fromJsons(j:String):Array[MCustomer] = {
//    import org.json4s.jackson.JsonMethods._
//    implicit val fmt = DefaultFormats
//    parse(j).extract[Array[MCustomer]]
//  }
}