package org.xg.dbModels

import org.xg.json.CommonUtils
import org.xg.pay.rewardPlan.RewardPlanSettings.VTag
import org.xg.pay.rewardPlan.RewardPlanSettings.VTag._
import org.xg.pay.rewardPlan.{RewardPlanSettings, TRewardPlan}

case class MRewardPlan(id:String, info:String, defi:String, vtag:String) {
  private val _vtag:VTag = VTag.withName(vtag)
  def getVTag:VTag = _vtag
  private val _plan:TRewardPlan = RewardPlanSettings.decodePlan(getVTag, defi)
  def getPlan:TRewardPlan = _plan
}

object MRewardPlan {
  def fromJsons(j:String):Array[MRewardPlan] = CommonUtils._fromJsons(j)
}
