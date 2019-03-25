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
  val Status_Created = 1
  val Status_Paid = 2
  val Status_Cancelled = 3

  def toJsons(stats:Array[MOrgOrderStat]):String = {
    CommonUtils._toJsons(stats)
  }

  def fromJsons(j:String):Array[MOrgOrderStat] = CommonUtils._fromJsons(j)
}