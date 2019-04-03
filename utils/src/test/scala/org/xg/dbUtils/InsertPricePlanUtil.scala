package org.xg.dbUtils

import org.xg.dbModels.MPricePlan
import org.xg.json.CommonUtils
import org.xg.pay.pricePlan.TPricePlan
import org.xg.pay.pricePlan.v1.{PrPlFixedRate, PrPlProdBasedRates}

object InsertPricePlanUtil {

  import org.xg.pay.pricePlan.PricePlanSettings.VTag._
  import MPricePlan._

  val PlanIdFixed08:String = "Fixed-0.8"
  val PlanIdFixed09:String = "Fixed-0.9"
  val PlanIdProdBasedBasic:String = "ProdBased-Basic"
  val PlanIdProdBasedAdvanced:String = "ProdBased-Advanced"
  val plans:Array[(String, String, VTag, TPricePlan, String)] = Array(
    (PlanIdFixed08, "Fixed Rate 80%", FixedRate, PrPlFixedRate(0.8), builtInCreator),
    (PlanIdFixed09, "Fixed Rate 90%", FixedRate, PrPlFixedRate(0.9), builtInCreator),
    (PlanIdProdBasedBasic, "Producted Based Basic, range: 80% - 90%", ProductBasedRates,
      PrPlProdBasedRates(0.9, Map(1 -> 0.8, 2 -> 0.85)), builtInCreator),
    (PlanIdProdBasedAdvanced, "Producted Based Advanced, range: 75% - 85%", ProductBasedRates,
      PrPlProdBasedRates(0.85, Map(1 -> 0.8, 2 -> 0.75)), builtInCreator),
  )

  def main(args:Array[String]):Unit = {

    insertPricePlans(plans)

  }

  def insertPricePlans(data:Array[(String, String, VTag, TPricePlan, String)]):Unit = {
    val insertStatementTemplate =
      "INSERT INTO price_plans (id, info, defi, vtag, creator)" +
        "  VALUES (%s);"

    val insertStatements = data.map { p =>
      val (id, info, vtag, plan, creator) = p
      val params = List(
        id, info, CommonUtils._toJson(plan), vtag.toString, creator
      ).mkString("'", "','", "'")
      insertStatementTemplate.format(params)
    }

    println(insertStatements.mkString("\n"))
  }
}
