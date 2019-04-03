package org.xg.srcTests

import org.xg.auth.SvcHelpers
import org.xg.dbModels.{MPricePlan, MRewardPlan}
import org.xg.gnl.GlobalCfg
import org.xg.json.CommonUtils
import org.xg.pay.rewardPlan.RewardPlanSettings.VTag
import org.xg.pay.rewardPlan.v1.RwPlFixedRate
import org.xg.srcTests.PricePlanTests.{cfg, newPPId, resp, uid}
import org.xg.user.UserType

object RewardPlanTests extends App {
  val cfg = GlobalCfg.localTestCfg
  //val authUrl = .authURL // "https://localhost:8443/webapi/auth/userPass"

  val (uid, pass) = UserType.Customer.genUid("customer1") -> "123"
  //   val (uid, pass) = "customer2" -> "456"
  //val (uid, pass) = "customer3" -> "abcdef"
  //val (uid, pass) = "customer4" -> "acf"
  val resp = SvcHelpers.authReq(cfg.authCustomerURL, uid, pass)

  if (resp.success)
    println(s"Success! Token: ${resp.token}")
  else
    println(s"Failed! Message: ${resp.msg}")

  val newRPId = SvcHelpers.addRewardPlan(
    cfg.addRewardPlanURL,
    resp.token,
    MRewardPlan(
      "test_rewardplan1",
      "test info1",
      CommonUtils._toJson(
        RwPlFixedRate(0.2)
      ),
      VTag.FixedRate.toString,
      uid
    )
  )

  println(newRPId)

  val plansByUser = SvcHelpers.pricePlansBy(
    cfg.rewardPlanCreatedByURL,
    resp.token,
    uid
  )

  println(plansByUser.length)
}
