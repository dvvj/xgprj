package org.xg.pay.rewardPlan.v1

import org.xg.pay.rewardPlan.TRewardPlan

case class RwPlFixedRate(globalRate:Double) extends TRewardPlan {
  override def reward(prodId: Int, price0: Double): Double = {
    price0*globalRate
  }
}
