package org.xg.busiLogic

import org.xg.dbModels._
import org.xg.gnl.DataUtils.{utcTimeNow, zonedDateTime2Ms}
import org.xg.pay.rewardPlan.RewardPlanSettings.VTag.VTag
import org.xg.pay.rewardPlan.TRewardPlan
import org.xg.pay.rewardPlan.v1.RwPlChained
import org.xg.svc.SvcCommonUtils

object RewardPlanLogics {
  def activeRewardPlans(allPlans:Array[MRewardPlanMap]):Map[String, MRewardPlanMap] = {
    allPlans.groupBy(_.uid).flatMap { p =>
      val pps = p._2.filter { ppm =>
        if (ppm.getExpireTime.nonEmpty) {
          val expTime = ppm.getExpireTime.get
          expTime.compareTo(utcTimeNow) > 0
        }
        else
          true
      }
      if (pps.nonEmpty)
        Option(p._1 -> pps.maxBy(ppm => zonedDateTime2Ms(ppm.getStartTime)))
      else
        None
    }
  }

  def rewardPlanFor(uid:String, planMap:Map[String, MRewardPlanMap], plans:Map[String, MRewardPlan]):Option[TRewardPlan] = {
    val planIds =
      if (planMap.contains(uid)) planMap(uid).getPlanIds
      else Array[String]()

    if (planIds.nonEmpty) {
      val res =
        if (planIds.length == 1)
          plans(planIds(0)).getPlan
        else {
          val chain = planIds.map(plans).map(_.getPlan).toList
          RwPlChained.create(chain)
        }
      Option(res)
    }
    else None
  }

  import collection.JavaConverters._
  def rewardPlanForJ(uid:String, planMapJ:java.util.Map[String, MRewardPlanMap], plansJ:java.util.Map[String, MRewardPlan]):TRewardPlan = {
    val planMap = planMapJ.asScala.toMap
    val plans = plansJ.asScala.toMap
    rewardPlanFor(uid, planMap, plans).orNull
  }

  def rewardPlanJsonFor(
    uid:String,
    planMap:Map[String,
    MRewardPlanMap], plans:Map[String, MRewardPlan]
  ):Array[(VTag, String)] = {
    val planIds =
      if (planMap.contains(uid)) planMap(uid).getPlanIds
      else Array[String]()

    planIds.map(plans).map { p =>
      p.getVTag -> p.defi
    }
  }

  import collection.JavaConverters._
  def rewardPlanJsonForJ(
                          uid:String,
                          planMap:java.util.Map[String, MRewardPlanMap],
                          plans:java.util.Map[String, MRewardPlan]
                        ):String = {
    val res = rewardPlanJsonFor(uid, planMap.asScala.toMap, plans.asScala.toMap)
    SvcCommonUtils.RewardPlanJson.convert2Json(res)
  }
}
