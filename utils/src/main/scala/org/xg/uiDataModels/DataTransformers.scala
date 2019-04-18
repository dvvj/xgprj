package org.xg.uiDataModels

import org.xg.dbModels.MProduct
import org.xg.pay.pricePlan.TPricePlan
import org.xg.uiModels.{Product => UiProduct}

object DataTransformers {

  def getProductMap(products:Array[MProduct], pricePlan:TPricePlan):Map[Int, UiProduct] = {
    products.map {mp =>
      val p = UiProduct.fromMProduct(mp, pricePlan)
      p.getId.toInt -> p
    }.toMap
  }

  def getProductMapJ(products:Array[MProduct], pricePlan:TPricePlan):Map[Integer, UiProduct] = {
    products.map {mp =>
      val p = UiProduct.fromMProduct(mp, pricePlan)
      p.getId -> p
    }.toMap
  }

}
