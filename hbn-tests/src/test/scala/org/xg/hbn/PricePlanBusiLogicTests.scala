package org.xg.hbn

import org.xg.busiLogic.PricePlanLogics
import org.xg.dbModels.{MCustomer, MPricePlan, MPricePlanMap}
import org.xg.hbn.HbnDbOpsImpl.testHbnOps
import org.xg.pay.pricePlan.TPricePlan

object PricePlanBusiLogicTests extends App {
  private def getAllPlans():Map[String, MPricePlan] = {
    val pricePlans = testHbnOps.allPricePlans
    println(pricePlans.length)
    pricePlans.map(pp => pp.id -> pp).toMap
  }

  val planMap = getAllPlans()
  val activePPM = testHbnOps.allActivePricePlans
  private def getPlan4(customer:MCustomer):Option[TPricePlan] = {
    PricePlanLogics.pricePlanFor(customer, activePPM, planMap)
  }

  val allCustomers = testHbnOps.allCustomers

  val testProds = List(1, 2, 3, 4)

  allCustomers.foreach { customer =>
    val plan = getPlan4(customer)
    if (plan.nonEmpty) {
      println(s"Plan found for customer [${customer.uid}]")
      testProds.foreach { prodId =>
        val resPrice = plan.get.adjust(prodId, 100.0)
        println(s"\t100.0 -> $resPrice")
      }
    }
    else {
      println(s"No plan for customer [${customer.uid}]")
    }
  }

}
