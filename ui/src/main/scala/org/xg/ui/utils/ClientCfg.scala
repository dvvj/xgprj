package org.xg.ui.utils

import java.io.FileOutputStream
import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.xg.gnl.GlobalCfg
import org.xg.json.CommonUtils

case class UserCfg(
  uid:String,
  utype:Integer,
  passHash:String
)

case class ClientCfg(
  userCfg: UserCfg,
  serverCfg:GlobalCfg
)

object ClientCfg {
  def fromJson(j:String):ClientCfg = CommonUtils._fromJson[ClientCfg](j)
  def toJson(cfg:ClientCfg):String = CommonUtils._toJson(cfg)
  def save(cfg:ClientCfg, path:String):Unit = {
    val os = new FileOutputStream(path)
    IOUtils.write(
      toJson(cfg),
      os,
      StandardCharsets.UTF_8
    )
    os.close()
  }
}