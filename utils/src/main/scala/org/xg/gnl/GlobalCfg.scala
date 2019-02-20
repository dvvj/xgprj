package org.xg.gnl

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.json4s.DefaultFormats

case class GlobalCfg(
  svcSvr:String
) {
  import GlobalCfg.REQ_AUTH
  def authURL:String = s"$svcSvr/$REQ_AUTH"
}

object GlobalCfg {
  private val REQ_AUTH = "auth/userPass"

  def fromJson(j:String):GlobalCfg = {
    import org.json4s.jackson.JsonMethods._
    implicit val fmt = DefaultFormats
    parse(j).extract[GlobalCfg]
  }

  val localTestCfg:GlobalCfg = {
    val strm = getClass.getResourceAsStream("/localTestCfg.json")
    val j = IOUtils.toString(strm, StandardCharsets.UTF_8)
    strm.close()
    fromJson(j)
  }
}