package org.xg.dbModels

import org.xg.json.CommonUtils

case class MOrgAgentOrderStat(
  orgAgentId:String,
  profId:String,
  orderId:Long,
  productId:Int,
  qty:Double,
  actualCost:Double,
  creationTimeS:String,
  status:Int
)

object MOrgAgentOrderStat {


  def toJsons(stats:Array[MOrgAgentOrderStat]):String = {
    CommonUtils._toJsons(stats)
  }

  def fromJsons(j:String):Array[MOrgAgentOrderStat] = CommonUtils._fromJsons(j)
}