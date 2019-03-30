package org.xg.ui.utils

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.xg.gnl.GlobalCfg
import org.xg.json.CommonUtils

case class UserCfg(
  uid:String,
  utype:Integer,
  passHash:String,
  svcSvr:String,
  timeOutMs:Int
)
//
//case class ClientCfg(
//  userCfg: UserCfg,
//  serverCfg:GlobalCfg
//)

object UserCfg {
  def fromJson(j:String):UserCfg = CommonUtils._fromJson[UserCfg](j)
  def toJson(cfg:UserCfg):String = CommonUtils._toJson(cfg)
  def save(cfg:UserCfg, path:String):Unit = {
    val os = new FileOutputStream(path)
    IOUtils.write(
      toJson(cfg),
      os,
      StandardCharsets.UTF_8
    )
    os.close()
  }

  def loadFromCurrDir():UserCfg = {
    val cfgFile = new File(System.getProperty("user.dir"), "client.cfg")
    val strm = new FileInputStream(cfgFile)
    val j = IOUtils.toString(strm, StandardCharsets.UTF_8)
    strm.close()
    fromJson(j)
  }
}