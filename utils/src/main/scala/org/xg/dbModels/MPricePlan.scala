package org.xg.dbModels

import org.xg.json.CommonUtils
import org.xg.json.CommonUtils._fromJsons
import org.xg.pay.pricePlan.PricePlanSettings.VTag
import org.xg.pay.pricePlan.PricePlanSettings.VTag._
import org.xg.pay.pricePlan.{PricePlanSettings, TPricePlan}

case class MPricePlan(id:String, info:String, defi:String, vtag:String, creator:String) {
  private val _vtag:VTag = VTag.withName(vtag)
  def getVTag:VTag = _vtag
  private val _plan:TPricePlan = PricePlanSettings.decodePlan(getVTag, defi)
  def getPlan:TPricePlan = _plan
}

object MPricePlan {
  def fromJsons(j:String):Array[MPricePlan] = _fromJsons(j)
  def fromJson(j:String):MPricePlan = CommonUtils._fromJson[MPricePlan](j)

  def toJson(p:MPricePlan):String = CommonUtils._toJson(p)
  def toJsons(p:Array[MPricePlan]):String = CommonUtils._toJsons(p)

  private val UnderScoreUnicode:String = "＿"
  val builtInCreator:String = s"${UnderScoreUnicode}global"
}