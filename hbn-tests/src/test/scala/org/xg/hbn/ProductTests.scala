package org.xg.hbn

import org.xg.hbn.utils.HbnUtils

object ProductTests extends App {
  import HbnDbOpsImpl._

  val products = testHbnOps.allProducts

  println(products.length)

  HbnUtils.shutdownTest()
}
