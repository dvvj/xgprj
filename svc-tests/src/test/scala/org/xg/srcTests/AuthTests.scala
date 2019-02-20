package org.xg.srcTests

import org.xg.auth.{AuthResp, SvcHelpers}
import org.xg.gnl.GlobalCfg

object AuthTests extends App {

  val cfg = GlobalCfg.localTestCfg
  //val authUrl = .authURL // "https://localhost:8443/webapi/auth/userPass"

  val resp = SvcHelpers.authReq(cfg.authURL, "customer1", "23")

  if (resp.success)
    println(s"Success! Token: ${resp.token}")
  else
    println(s"Failed! Message: ${resp.msg}")

}
