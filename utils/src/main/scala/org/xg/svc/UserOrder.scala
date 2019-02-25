package org.xg.svc

import org.json4s.DefaultFormats
import org.xg.json.CommonUtils

case class UserOrder(uid:String, productId:Int, qty:Double)

object UserOrder {
  def toJson(upp:UserOrder):String = {
    CommonUtils._toJson(upp)
  }

  def fromJson(json:String):UserOrder = {
    CommonUtils._fromJson[UserOrder](json)
  }

}
