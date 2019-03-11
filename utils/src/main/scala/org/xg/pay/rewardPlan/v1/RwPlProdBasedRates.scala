package org.xg.pay.rewardPlan.v1

import org.xg.pay.rewardPlan.TRewardPlan

case class RwPlProdBasedRates(
  globalRate:Double,
  prodRates:Map[Int, Double]
) extends TRewardPlan {
  override def reward(prodId: Int, price0: Double): Double = {
    val rate = prodRates.getOrElse(prodId, globalRate)
    price0*rate
  }
}
