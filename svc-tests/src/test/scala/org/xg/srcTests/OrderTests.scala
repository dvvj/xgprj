package org.xg.srcTests

import org.xg.auth.SvcHelpers
import org.xg.db.model.MOrder
import org.xg.gnl.GlobalCfg

object OrderTests extends App {

  val cfg = GlobalCfg.localTestCfg

  def authUser(uid:String, pass:String):String = {
    val resp = SvcHelpers.authReq(cfg.authURL, uid, pass)
    if (!resp.success)
      throw new RuntimeException(s"Failed to authenticate with [$uid]-[$pass]")

    resp.token
  }

  val (uid, pass) = "customer1" -> "123"
  val token = authUser(uid, pass)

  val currOrders = SvcHelpers.getDecArray(cfg.currOrdersURL, token, MOrder.fromJsons)
  println(s"currOrders:\n${currOrders.mkString("\n")}")


}
