package org.xg.srcTests

import org.xg.auth.SvcHelpers
import org.xg.dbModels.{MOrder, MOrgOrderStat}
import org.xg.gnl.GlobalCfg
import org.xg.user.UserType

object OrderStatsOfOrgTests extends App {

  val cfg = GlobalCfg.localTestCfg

  def authUser(uid:String, pass:String):String = {
    val resp = SvcHelpers.authReq(cfg.authProfOrgURL, uid, pass)
    if (!resp.success)
      throw new RuntimeException(s"Failed to authenticate with [$uid]-[$pass]")

    resp.token
  }

  val (uid, pass) = UserType.MedProfOrg.genUid("proforg1") -> "123"
  val token = authUser(uid, pass)

  val orderStats = SvcHelpers.postDecArray(cfg.orderStatsOfOrgURL, token, uid, MOrgOrderStat.fromJsons)
  println(s"orderStats:\n${orderStats.mkString("\n")}")


}
