package org.xg.dbModels

import org.xg.json.CommonUtils

case class MOrgOrderStat(
  orgId:String,
  profId:String,
  orderId:Long,
  productId:Int,
  qty:Double,
  actualCost:Double,
  creationTimeS:String,
  status:Int
)

object MOrgOrderStat {
  val Status_Cancelled:Int = -1
  val Status_CreatedNotPaid:Int = 1
  val Status_Paid:Int = 2
  val Status_Locked:Int = 3

  def toJsons(stats:Array[MOrgOrderStat]):String = {
    CommonUtils._toJsons(stats)
  }

  def fromJsons(j:String):Array[MOrgOrderStat] = CommonUtils._fromJsons(j)
}