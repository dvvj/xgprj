package org.xg.pay.rewardPlan.v1

import org.xg.pay.rewardPlan.TRewardPlan

case class RwPlChained(planList:List[TRewardPlan]) extends TRewardPlan {
  override def reward(prodId: Int, price0: Double): Double = {
    var res = price0
    planList.foreach(p => res = p.reward(prodId, res))
    res
  }
}

object RwPlChained {
  def create(plans:List[TRewardPlan]):TRewardPlan = {
    RwPlChained(plans)
  }
}