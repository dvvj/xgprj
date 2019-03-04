package org.xg.svc

import org.xg.json.CommonUtils

case class PayOrder(orderId:Long, uid:String)

object PayOrder {
  def toJson(po:PayOrder):String = {
    CommonUtils._toJson(po)
  }

  def fromJson(json:String):PayOrder = {
    CommonUtils._fromJson[PayOrder](json)
  }
}
