package org.xg.svc

import org.xg.json.CommonUtils
import org.xg.pay.pricePlan.PricePlanSettings.VTag.VTag

case class AddPricePlanJson(
  apply2Uid:String,
  planId:String,
  createdBy:String,
  vtag:VTag,
  defi:String
)

object AddPricePlanJson {
  def toJson(app:AddPricePlanJson):String = CommonUtils._toJson(app)
  def fromJson(j:String):AddPricePlanJson = CommonUtils._fromJson[AddPricePlanJson](j)
}