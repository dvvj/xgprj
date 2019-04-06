package org.xg.srcTests

import org.xg.auth.SvcHelpers
import org.xg.gnl.GlobalCfg

object PayOpsTests extends App {

  val r = SvcHelpers.postWithContentType(
    GlobalCfg.localTestCfg.alipayNotifyURL,
    "",
    //"text/html;charset=utf-8",
    "text/html;charset=utf-8",
    "11fgfgttt"
  )

  println(r)

}
