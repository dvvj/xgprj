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

  def main(args:Array[String]):Unit = {
    //insertAllUsers()
    println()
    insertOrders()
  }
}
