package org.xg.dbUtils

import org.xg.gnl.DataUtils

object InsertRewardPlanMapUtil extends App {
  import InsertRewardPlanUtil._
  import InsertMedProfsUtil._
  import InsertCustomersUtil._
  val rpmaps = Array(
    (profOrgAgentId1, PlanIdFixed020, 0),
    (profOrgAgentId2, PlanIdFixed020, 0),
    (profId1, PlanIdFixed001, -7),
    (profId1, PlanIdFixed002, 0),
    (profId2, s"$PlanIdProdBasedBasic,$PlanIdFixed1_5", 0),
    (customerId1, PlanIdProdBasedAdvanced, 0)
  )

  def insertPlanMap(mapData:Array[(String, String, Int)]):Unit = {
    val insertStatementTemplate =
      "INSERT INTO reward_plan_map (uid, plan_ids, start_time)" +
        "  VALUES (%s);"

    val insertStatements = mapData.map { ppm =>
      val (uid, planIds, days) = ppm
      val dt = DataUtils.zonedDateTime2Str(DataUtils.utcTimeNow.plusDays(days))
      val params = Array(
        uid, planIds, dt
      ).mkString("'", "','", "'")
      insertStatementTemplate.format(params)
      //MPricePlanMap(uid, planIds, dt, null)
    }

    println(insertStatements.mkString("\n"))
  }

  insertPlanMap(rpmaps)

}
