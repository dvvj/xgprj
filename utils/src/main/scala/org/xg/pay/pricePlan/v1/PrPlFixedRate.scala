package org.xg.pay.pricePlan.v1

import org.xg.pay.pricePlan.TPricePlan

case class PrPlFixedRate(globalRate:Double) extends TPricePlan {
  override def adjust(prodId:Int, price0: Double): Double = price0*globalRate
}
