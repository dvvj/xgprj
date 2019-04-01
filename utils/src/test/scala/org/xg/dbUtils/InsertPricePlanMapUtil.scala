package org.xg.dbUtils

import org.xg.dbModels.MPricePlanMap
import org.xg.gnl.DataUtils

object InsertPricePlanMapUtil extends App {

  import InsertPricePlanUtil._
  import InsertMedProfsUtil._
  import InsertCustomersUtil._
  val ppmaps = Array(
    (profId1, PlanIdFixed09, -7),
    (profId1, PlanIdFixed08, 0),
    (profId2, s"$PlanIdProdBasedBasic,$PlanIdFixed09", 0),
    (customerId1, PlanIdProdBasedAdvanced, 0)
  )

  val insertStatementTemplate =
    "INSERT INTO price_plan_map (uid, plan_ids, start_time)" +
      "  VALUES (%s);"

  val insertStatements = ppmaps.map { ppm =>
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
