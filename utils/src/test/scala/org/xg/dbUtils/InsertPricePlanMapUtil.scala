package org.xg.dbUtils

import org.xg.dbModels.MPricePlanMap
import org.xg.gnl.DataUtils

object InsertPricePlanMapUtil extends App {

  import InsertPricePlanUtil._
  val ppmaps = Array(
    ("prof1", PlanIdFixed09, -7),
    ("prof1", PlanIdFixed08, 0),
    ("prof2", s"$PlanIdProdBasedBasic,$PlanIdFixed09", 0),
    ("customer1", PlanIdProdBasedAdvanced, 0)
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
