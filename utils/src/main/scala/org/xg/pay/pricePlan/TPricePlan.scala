package org.xg.pay.pricePlan

trait TPricePlan {
  def adjust(prodId:Int, price0:Double):Double
}
