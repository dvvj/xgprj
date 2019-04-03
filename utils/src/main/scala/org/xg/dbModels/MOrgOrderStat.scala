package org.xg.dbModels

import org.xg.json.CommonUtils

case class MOrgOrderStat(
  orgId:String,
  orgAgentId:String,
  profId:String,
  orderId:Long,
  productId:Int,
  qty:Double,
  actualCost:Double,
  creationTimeS:String,
  status:Int
)

object MOrgOrderStat {
  def toJsons(stats:Array[MOrgOrderStat]):String = {
    CommonUtils._toJsons(stats)
  }

  def fromJsons(j:String):Array[MOrgOrderStat] = CommonUtils._fromJsons(j)

}