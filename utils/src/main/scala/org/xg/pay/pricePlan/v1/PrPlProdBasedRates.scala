package org.xg.pay.pricePlan.v1

import org.xg.pay.pricePlan.TPricePlan

case class PrPlProdBasedRates (
  globalRate:Double,
  prodRates:Map[Int, Double]
) extends TPricePlan {
  override def adjust(prodId:Int, price0: Double): Double = {
    val rate = prodRates.getOrElse(prodId, globalRate)
    price0*rate
  }
}
