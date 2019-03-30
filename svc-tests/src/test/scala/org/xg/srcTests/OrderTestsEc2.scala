package org.xg.srcTests

import org.xg.auth.SvcHelpers
import org.xg.dbModels.MOrder
import org.xg.gnl.GlobalCfg

object OrderTestsEc2 extends App {

  val ec2Cfg = """{
                 |  "svcSvr": "https://54.229.5.233:8443/webapi",
                 |  "timeOutMs": 1200,
                 |  "infoDbSvr": "jdbc:mysql://172.17.0.1:3306",
                 |  "assetLocalPath": "/appdata/product_assets"
                 |}""".stripMargin
  val cfg = GlobalCfg.fromJson(ec2Cfg)

  def authUser(uid:String, pass:String):String = {
    val resp = SvcHelpers.authReq(cfg.authCustomerURL, uid, pass)
    if (!resp.success)
      throw new RuntimeException(s"Failed to authenticate with [$uid]-[$pass]")

    resp.token
  }

  val (uid, pass) = "customer1" -> "123"
  val token = authUser(uid, pass)

  val currOrders = SvcHelpers.getDecArray(cfg.currOrdersURL, token, MOrder.fromJsons)
  println(s"currOrders:\n${currOrders.mkString("\n")}")
}
