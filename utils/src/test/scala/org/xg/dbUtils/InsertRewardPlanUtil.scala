package org.xg.dbUtils

import org.xg.json.CommonUtils
import org.xg.pay.rewardPlan.TRewardPlan
import org.xg.pay.rewardPlan.v1.{RwPlFixedRate, RwPlProdBasedRates}

object InsertRewardPlanUtil {
  import org.xg.pay.rewardPlan.RewardPlanSettings.VTag._

  val PlanIdFixed001:String = "Fixed-0.01"
  val PlanIdFixed002:String = "Fixed-0.02"
  val PlanIdFixed020:String = "Fixed-0.20"
  val PlanIdFixed1_5:String = "Fixed-1.5"
  val PlanIdProdBasedBasic:String = "ProdBased-Basic"
  val PlanIdProdBasedAdvanced:String = "ProdBased-Advanced"

  val plans:Array[(String, String, VTag, TRewardPlan)] = Array(
    (PlanIdFixed001, "Fixed Rate 1%", FixedRate, RwPlFixedRate(0.01)),
    (PlanIdFixed002, "Fixed Rate 2%", FixedRate, RwPlFixedRate(0.02)),
    (PlanIdFixed020, "Fixed Rate 20%", FixedRate, RwPlFixedRate(0.2)),
    (PlanIdFixed1_5, "Fixed Rate x 1.5, used only in combination with other plans", FixedRate, RwPlFixedRate(1.5)),
    (PlanIdProdBasedBasic, "Producted Based Basic, range: 1% - 3%", ProductBasedRates,
      RwPlProdBasedRates(0.01, Map(1 -> 0.02, 2 -> 0.03))),
    (PlanIdProdBasedAdvanced, "Producted Based Advanced, range: 2% - 5%", ProductBasedRates,
      RwPlProdBasedRates(0.02, Map(1 -> 0.03, 2 -> 0.05))),
  )
  def main(args:Array[String]):Unit = {
    insertPlans(plans)
  }

  def insertPlans(planData:Array[(String, String, VTag, TRewardPlan)]):Unit = {
    val insertStatementTemplate =
      "INSERT INTO reward_plans (id, info, defi, vtag)" +
        "  VALUES (%s);"

    val insertStatements = planData.map { p =>
      val (id, info, vtag, plan) = p
      val params = List(
        id, info, CommonUtils._toJson(plan), vtag.toString
      ).mkString("'", "','", "'")
      insertStatementTemplate.format(params)
    }

    println(insertStatements.mkString("\n"))
  }
}
