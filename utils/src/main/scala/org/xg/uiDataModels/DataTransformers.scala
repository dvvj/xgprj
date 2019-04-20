package org.xg.uiDataModels

import org.xg.dbModels.MProduct
import org.xg.pay.pricePlan.TPricePlan
import org.xg.uiModels.UIProduct

object DataTransformers {

  def getProductMap(products:Array[MProduct], pricePlanMap:Map[Int, TPricePlan]):Map[Int, UIProduct] = {
    products.map {mp =>
      val p = UIProduct.fromMProduct(mp, pricePlanMap.get(mp.id).orNull)
      p.getId.toInt -> p
    }.toMap
  }

  def getProductMapJ(products:Array[MProduct], pricePlanMap:Map[Integer, TPricePlan]):Map[Integer, UIProduct] = {
    products.map {mp =>
      val p = UIProduct.fromMProduct(mp, pricePlanMap.get(mp.id).orNull)
      p.getId -> p
    }.toMap
  }

}
