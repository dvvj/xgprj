package org.xg.hbn

import org.xg.hbn.utils.HbnUtils

object PricePlanTests extends App {
  import HbnDbOpsImpl._

  val pricePlans = testHbnOps.allPricePlans

  println(pricePlans.length)

  HbnUtils.shutdownTest()
}