package org.xg.ui.utils

import org.xg.db.model.MProduct
import org.xg.ui.model.Product

object Helpers {
  def convProducts(mps:Array[MProduct]):Array[Product] = {
    mps.map(Product.fromMProduct)
  }
}
