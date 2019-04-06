package org.xg.srcTests

import org.xg.auth.SvcHelpers
import org.xg.gnl.GlobalCfg

object PayOpsTests extends App {

  val r = SvcHelpers.postWithContentType(
    //GlobalCfg.localTestCfg.alipayNotifyURL,
//    "https://52.31.212.53:443/webapi/payment/alipayReturn",
    "https://wonder4.life:443/webapi/payment/alipayNotify",
    "",
    //"text/html;charset=utf-8",
    "application/x-www-form-urlencoded;charset=utf-8",
//    "text/html;charset=utf-8",
    "11fgfgttt"
  )

  println(r)

}
