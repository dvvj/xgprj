package org.xg.svc

import org.json4s.DefaultFormats

case class UserOrder(uid:String, productId:Int, qty:Double)

object UserOrder {
  def toJson(upp:UserOrder):String = {
    import org.json4s.jackson.Serialization._
    write(upp)(DefaultFormats)
  }

  def fromJson(json:String):UserOrder = {
    import org.json4s.jackson.JsonMethods._
    implicit val _fmt = DefaultFormats
    parse(json).extract[UserOrder]
  }

}
