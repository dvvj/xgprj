package org.xg.uiDataModels

import org.xg.dbModels.{MMedProf, MProduct}
import org.xg.pay.pricePlan.TPricePlan
import org.xg.uiModels.{CustomerProduct, MedProf, UIProduct}

object DataTransformers {

  def getProductMap(products:Array[MProduct], pricePlanMap:Map[Int, TPricePlan]):Map[Int, UIProduct] = {
    products.map {mp =>
      val p = UIProduct.fromMProduct(mp)
      p.getId.toInt -> p
    }.toMap
  }

  def getProductMapJ(
                      products:Array[MProduct],
                      pricePlanMap:Map[Integer, TPricePlan],
                      prod2Prof:Map[Int, MMedProf]
                    ):Map[Integer, CustomerProduct] = {
    products.map {mp =>
      val pricePlan = pricePlanMap.get(mp.id).orNull
      val p = UIProduct.fromMProduct(mp)
      val mprof = prod2Prof.get(mp.id)
      val prof = mprof.map(MedProf.fromMMedProf).orNull
      val cp = new CustomerProduct(p, pricePlan, prof)
      p.getId -> cp
    }.toMap
  }

}
