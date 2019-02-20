package org.xg.srcTests

import org.xg.auth.{AuthResp, SvcHelpers}
import org.xg.gnl.GlobalCfg

object AuthTests extends App {

  val cfg = GlobalCfg.localTestCfg
  //val authUrl = .authURL // "https://localhost:8443/webapi/auth/userPass"

  val resp = SvcHelpers.authReq(cfg.authURL, "customer1", "123")

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
}
