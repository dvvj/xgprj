package org.xg.db.model

import java.time.ZonedDateTime

case class MOrder(
  id:Long,
  uid:String,
  productId:Int,
  qty:Double,
  creationTimeS:String,
  procTime1S:String,
  procTime2S:String,
  procTime3S:String
) {
//  def creationTime:ZonedDateTime = {
//
//  }
}

object MOrder {
  import org.xg.json.CommonUtils._
  def toJsons(customers:Array[MOrder]):String = _toJsons(customers)
  def fromJsons(j:String):Array[MOrder] = _fromJsons(j)
}