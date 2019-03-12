package org.xg.busiLogic

import org.xg.dbModels._
import org.xg.gnl.DataUtils.{utcTimeNow, zonedDateTime2Ms}
import org.xg.pay.rewardPlan.TRewardPlan
import org.xg.pay.rewardPlan.v1.RwPlChained

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

}
