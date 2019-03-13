package org.xg.srcTests

import org.xg.auth.{AuthResp, SvcHelpers}
import org.xg.dbModels._
import org.xg.gnl.GlobalCfg
import org.xg.svc.UserOrder

object AuthTests extends App {

  val cfg = GlobalCfg.localTestCfg
  //val authUrl = .authURL // "https://localhost:8443/webapi/auth/userPass"

  val (uid, pass) = "customer1" -> "123"
//   val (uid, pass) = "customer2" -> "456"
  //val (uid, pass) = "customer3" -> "abcdef"
  //val (uid, pass) = "customer4" -> "acf"
  val resp = SvcHelpers.authReq(cfg.authURL, uid, pass)

  if (resp.success)
    println(s"Success! Token: ${resp.token}")
  else
    println(s"Failed! Message: ${resp.msg}")

  val products = SvcHelpers.getDecArray(cfg.allProductsURL, resp.token, MProduct.fromJsons)
  println(s"products:\n${products.mkString("\n")}")

//  val customers1 = SvcHelpers.get(cfg.allCustomersURL)
//  println(s"customers: $customers1")
  val customers2 = SvcHelpers.getDecArray(cfg.allCustomersURL, resp.token, MCustomer.fromJsons)
  println(s"customers:\n${customers2.mkString("\n")}")
  val currOrders = SvcHelpers.getDecArray(cfg.currOrdersURL, resp.token, MOrder.fromJsons)
  println(s"currOrders:\n${currOrders.mkString("\n")}")

  val pricePlans = SvcHelpers.post(cfg.pricePlanURL, resp.token, uid)
  println(pricePlans)

  val (uid2, pass2) = "customer4" -> "acf"
  val resp2 = SvcHelpers.authReq(cfg.authURL, uid2, pass2)

  val pricePlans2 = SvcHelpers.getPricePlan4User(cfg.pricePlanURL, uid2, resp2.token)
  println(pricePlans2)

  (1 to 4).foreach { prodId =>
    println(
      pricePlans2.get.adjust(prodId, 100.0)
    )
  }

  val rewardPlan = SvcHelpers.getRewardPlan4User(cfg.rewardPlanURL, "prof2", resp2.token)
  println(rewardPlan)

  (1 to 4).foreach { prodId =>
    println(
      rewardPlan.get.reward(prodId, 100.0)
    )
  }

//  val orderJson = UserOrder.toJson(
//    UserOrder(
//      uid, 1, 2.0
//    )
//  )
//  val placeOrderRes = SvcHelpers.reqPut(
//    cfg.placeOrderURL, resp.token,
//    orderJson
//  )
//  println(s"placeOrderRes:\n$placeOrderRes")

}
