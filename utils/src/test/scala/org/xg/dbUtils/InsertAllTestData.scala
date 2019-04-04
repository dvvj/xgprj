package org.xg.dbUtils

import org.xg.dbUtils.TestDataSet._

object InsertAllTestData {

  private def insertAllUsers():Unit = {
    allCustomers.foreach { p =>
      println()
      val count = p._2
      insertProfData(p._1)
      insertCustomerData(p._1, count)
    }

    insertOrgAgentData()
  }

  private def insertOrders():Unit = {
    InsertOrderUtil.insertOrders(TestDataSet.orderData, customerId2ProfIdMap, profId2AgentIdMap)
  }

  private def insertRewardPlans():Unit = {
    InsertRewardPlanUtil.insertPlans(
      TestDataSet.RewardPlans.planData
    )
  }

  private def insertPricePlans():Unit = {
    InsertPricePlanUtil.insertPricePlans(
      TestDataSet.PricePlans.planData
    )
  }


  private def insertRewardPlanMap():Unit = {
    InsertRewardPlanMapUtil.insertPlanMap(
      TestDataSet.RewardPlans.planMapData
    )
  }

  def main(args:Array[String]):Unit = {
    //insertAllUsers()
//    println()
//    insertOrders()
//    println()
//    insertRewardPlans()
//    println()
//    insertRewardPlanMap()
    println()
    insertPricePlans()
  }
}
