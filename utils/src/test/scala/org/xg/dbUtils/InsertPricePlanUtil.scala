package org.xg.dbUtils

import org.xg.json.CommonUtils
import org.xg.pay.pricePlan.v1.{PrPlFixedRate, PrPlProdBasedRates}

object InsertPricePlanUtil {

  import org.xg.pay.pricePlan.PricePlanSettings.VTag._

  val PlanIdFixed08:String = "Fixed-0.8"
  val PlanIdFixed09:String = "Fixed-0.9"
  val PlanIdProdBasedBasic:String = "ProdBased-Basic"
  val PlanIdProdBasedAdvanced:String = "ProdBased-Advanced"
  val plans = Array(
    (PlanIdFixed08, "Fixed Rate 80%", FixedRate, PrPlFixedRate(0.8)),
    (PlanIdFixed09, "Fixed Rate 90%", FixedRate, PrPlFixedRate(0.9)),
    (PlanIdProdBasedBasic, "Producted Based Basic, range: 80% - 90%", ProductBasedRates,
      PrPlProdBasedRates(0.9, Map(1 -> 0.8, 2 -> 0.85))),
    (PlanIdProdBasedAdvanced, "Producted Based Advanced, range: 75% - 85%", ProductBasedRates,
      PrPlProdBasedRates(0.85, Map(1 -> 0.8, 2 -> 0.75))),
  )
  def main(args:Array[String]):Unit = {


    val insertStatementTemplate =
      "INSERT INTO price_plans (id, info, defi, vtag)" +
        "  VALUES (%s);"

    val insertStatements = plans.map { p =>
      val (id, info, vtag, plan) = p
      val params = List(
        id, info, CommonUtils._toJson(plan), vtag.toString
      ).mkString("'", "','", "'")
      insertStatementTemplate.format(params)
    }

    println(insertStatements.mkString("\n"))
  }

}
