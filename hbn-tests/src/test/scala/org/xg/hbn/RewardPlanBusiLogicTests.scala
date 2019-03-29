package org.xg.hbn

import org.xg.busiLogic.{PricePlanLogics, RewardPlanLogics}
import org.xg.dbModels.{MCustomer, MRewardPlan}
import org.xg.hbn.HbnDbOpsImpl.testHbnOps
import org.xg.pay.pricePlan.TPricePlan
import org.xg.pay.rewardPlan.TRewardPlan

object RewardPlanBusiLogicTests extends App {

  private def getAllPlans():Map[String, MRewardPlan] = {
    val plans = testHbnOps.allRewardPlans
    println(plans.length)
    plans.map(pp => pp.id -> pp).toMap
  }

  val planMap = getAllPlans()
  val activePPM = testHbnOps.allActiveRewardPlans
  private def getPlan4(uid:String):Option[TRewardPlan] = {
    RewardPlanLogics.rewardPlanFor(uid, activePPM, planMap)
  }

  val allProfs = testHbnOps.allMedProfs

  val testProds = List(1, 2, 3, 4)

  allProfs.foreach { prof =>
    val plan = getPlan4(prof.profId)
    if (plan.nonEmpty) {
      println(s"Plan found for med prof [${prof.profId}]")
      testProds.foreach { prodId =>
        val resPrice = plan.get.reward(prodId, 100.0)
        println(s"\t100.0 -> $resPrice")
      }
    }
    else {
      println(s"No plan for med prof [${prof.profId}]")
    }
  }

  val allOrgs = testHbnOps.allProfOrgs
  allOrgs.foreach { org =>
    val plan = getPlan4(org.orgId)
    if (plan.nonEmpty) {
      println(s"Plan found for med prof [${org.orgId}]")
      testProds.foreach { prodId =>
        val resPrice = plan.get.reward(prodId, 100.0)
        println(s"\t100.0 -> $resPrice")
      }
    }
    else {
      println(s"No plan for med prof [${org.orgId}]")
    }
  }
}
