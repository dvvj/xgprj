package org.xg.srcTests

import org.xg.auth.{AuthResp, SvcHelpers}
import org.xg.gnl.GlobalCfg
import org.xg.svc.UserOrder

object AuthTests extends App {

  val cfg = GlobalCfg.localTestCfg
  //val authUrl = .authURL // "https://localhost:8443/webapi/auth/userPass"

  val uid = "customer1"
  val resp = SvcHelpers.authReq(cfg.authURL, uid, "123")

  if (resp.success)
    println(s"Success! Token: ${resp.token}")
  else
    println(s"Failed! Message: ${resp.msg}")

  val products = SvcHelpers.reqGet(cfg.allProductsURL)
  println(s"products: $products")
  val customers1 = SvcHelpers.reqGet(cfg.allCustomersURL)
  println(s"customers: $customers1")
  val customers2 = SvcHelpers.reqGet(cfg.allCustomersURL, resp.token)
  println(s"customers: $customers2")
  val currOrders = SvcHelpers.reqPost(cfg.currOrdersURL, resp.token, uid)
  println(s"currOrders:\n$currOrders")

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
