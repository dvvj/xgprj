package org.xg.srcTests

import org.xg.auth.SvcHelpers
import org.xg.dbModels.MPricePlan
import org.xg.gnl.GlobalCfg
import org.xg.json.CommonUtils
import org.xg.pay.pricePlan.PricePlanSettings.VTag.FixedRate
import org.xg.pay.pricePlan.v1.PrPlFixedRate
import org.xg.uiModels.Customer
import org.xg.user.UserType

object PricePlanTests extends App {
  val cfg = GlobalCfg.localTestCfg
  //val authUrl = .authURL // "https://localhost:443/webapi/auth/userPass"

  val (uid, pass) = UserType.Customer.genUid("customer1") -> "123"
  //   val (uid, pass) = "customer2" -> "456"
  //val (uid, pass) = "customer3" -> "abcdef"
  //val (uid, pass) = "customer4" -> "acf"
  val resp = SvcHelpers.authReq(cfg.authCustomerURL, uid, pass)

  if (resp.success)
    println(s"Success! Token: ${resp.token}")
  else
    println(s"Failed! Message: ${resp.msg}")


  val newPPId = SvcHelpers.addPricePlan(
    cfg.addPricePlanURL,
    resp.token,
    MPricePlan(
      "test_priceplan2",
      "test info",
      CommonUtils._toJson(
        PrPlFixedRate(0.6)
      ),
      FixedRate.toString,
      uid
    )
  )

  println(newPPId)

  val plansByUser = SvcHelpers.pricePlansBy(
    cfg.pricePlanAccessibleByURL,
    resp.token,
    uid
  )

  println(plansByUser.length)
}
