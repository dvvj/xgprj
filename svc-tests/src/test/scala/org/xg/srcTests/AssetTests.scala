package org.xg.srcTests

import java.io.FileOutputStream

import org.apache.commons.io.IOUtils
import org.xg.auth.SvcHelpers
import org.xg.gnl.GlobalCfg
import org.xg.svc.ImageInfo

object AssetTests extends App {

  val cfg = GlobalCfg.localTestCfg

  val imgInfo = ImageInfo(1, "1.png")
  val reqJson = ImageInfo.toJson(imgInfo)
  val resp = SvcHelpers.post4Bin(
    cfg.imgAssetURL,
    "",
    reqJson
  )

  println(resp.length)
  //IOUtils.write(resp, new FileOutputStream("/tmp/ttt.png"))
}
