package org.xg.pay.pricePlan.v1

case class Rates(
  globalRate:Double,
  prodRates:Map[Int, Double]
)

object Rates {

}