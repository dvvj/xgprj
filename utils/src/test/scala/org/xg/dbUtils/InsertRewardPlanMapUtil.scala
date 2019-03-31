package org.xg.dbUtils

import org.xg.gnl.DataUtils

object InsertRewardPlanMapUtil extends App {
  import InsertRewardPlanUtil._
  val rpmaps = Array(
    ("prof_org_agent1", PlanIdFixed020, 0),
    ("prof_org_agent2", PlanIdFixed020, 0),
    ("prof1", PlanIdFixed001, -7),
    ("prof1", PlanIdFixed002, 0),
    ("prof2", s"$PlanIdProdBasedBasic,$PlanIdFixed1_5", 0),
    ("customer1", PlanIdProdBasedAdvanced, 0)
  )

  val insertStatementTemplate =
    "INSERT INTO reward_plan_map (uid, plan_ids, start_time)" +
      "  VALUES (%s);"

  val insertStatements = rpmaps.map { ppm =>
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
