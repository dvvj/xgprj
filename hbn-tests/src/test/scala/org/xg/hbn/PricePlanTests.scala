package org.xg.hbn

import org.xg.hbn.utils.HbnUtils
import org.xg.pay.pricePlan.TPricePlan

object PricePlanTests extends App {
  import HbnDbOpsImpl._

  private def traceAllPlans():Unit = {
    val pricePlans = testHbnOps.allPricePlans
    println(pricePlans.length)
  }

  private def getPlan4(uid:String):Unit = {
    val plans = testHbnOps.pricePlansByUid(uid)
    println(plans.length)
  }

  getPlan4("prof1")
  getPlan4("prof2")
  getPlan4("customer1rm .")

  HbnUtils.shutdownTest()
}