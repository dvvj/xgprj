package org.xg.dbModels

import org.xg.json.CommonUtils._fromJsons
import org.xg.pay.pricePlan.PricePlanSettings.VTag
import org.xg.pay.pricePlan.PricePlanSettings.VTag._
import org.xg.pay.pricePlan.{PricePlanSettings, TPricePlan}

case class MPricePlan(id:String, info:String, defi:String, vtag:String) {
  private val _vtag:VTag = VTag.withName(vtag)
  def getVTag:VTag = _vtag
  private val _plan:TPricePlan = PricePlanSettings.decodePlan(getVTag, defi)
  def getPlan:TPricePlan = _plan
}

object MPricePlan {
  def fromJsons(j:String):Array[MPricePlan] = _fromJsons(j)
}