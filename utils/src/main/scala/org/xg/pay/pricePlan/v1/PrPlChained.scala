package org.xg.pay.pricePlan.v1

import org.xg.pay.pricePlan.TPricePlan

case class PrPlChained(planList:List[TPricePlan]) extends TPricePlan {
  override def adjust(prodId: Int, price0: Double): Double = {
    var res = price0
    planList.foreach(p => res = p.adjust(prodId, res))
    res
  }
}

object PrPlChained {
  def create(plans:List[TPricePlan]):TPricePlan = {
    PrPlChained(plans.toList)
  }
}