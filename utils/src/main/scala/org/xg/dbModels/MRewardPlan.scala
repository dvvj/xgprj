package org.xg.dbModels

import org.xg.json.CommonUtils
import org.xg.pay.rewardPlan.RewardPlanSettings.VTag
import org.xg.pay.rewardPlan.RewardPlanSettings.VTag._
import org.xg.pay.rewardPlan.{RewardPlanSettings, TRewardPlan}

case class MRewardPlan(id:String, info:String, defi:String, vtag:String, creator:String) {
  private val _vtag:VTag = VTag.withName(vtag)
  def getVTag:VTag = _vtag
  private val _plan:TRewardPlan = RewardPlanSettings.decodePlan(getVTag, defi)
  def getPlan:TRewardPlan = _plan
}

object MRewardPlan {
  def fromJsons(j:String):Array[MRewardPlan] = CommonUtils._fromJsons(j)
  def fromJson(j:String):MRewardPlan = CommonUtils._fromJson[MRewardPlan](j)

  def toJson(p:MRewardPlan):String = CommonUtils._toJson(p)
  def toJsons(p:Array[MRewardPlan]):String = CommonUtils._toJsons(p)

  private val UnderScoreUnicode:String = "＿"
  val builtInCreator:String = s"${UnderScoreUnicode}global"
}
