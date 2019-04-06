package org.xg.srcTests

import org.xg.auth.SvcHelpers
import org.xg.gnl.GlobalCfg

object PayOpsTests extends App {

  val r = SvcHelpers.post(
    GlobalCfg.localTestCfg.alipayReturnURL,
    "",
    "11fgfgttt"
  )

  println(r)

}
