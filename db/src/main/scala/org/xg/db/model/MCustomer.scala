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
  def toJsons(customers:Array[MCustomer]):String = {
    import org.json4s.jackson.Serialization._
    write(customers)(DefaultFormats)
  }

  def fromJsons(j:String):Array[MCustomer] = {
    import org.json4s.jackson.JsonMethods._
    implicit val fmt = DefaultFormats
    parse(j).extract[Array[MCustomer]]
  }
}