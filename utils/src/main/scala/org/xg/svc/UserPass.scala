package org.xg.svc

import org.json4s.DefaultFormats
import org.xg.auth.AuthHelpers
import org.xg.json.CommonUtils

case class UserPass(uid:String, passHashStr:String)

object UserPass {
  //  def enc(uid:String, pass:Array[Byte]):UserPassBase64 = {
  //    UserPassBase64(
  //      uid,
  //      Base64.getEncoder.encodeToString(pass)
  //    )
  //  }

  def toJson(upp:UserPass):String = {
    CommonUtils._toJson(upp)
  }

  def fromJson(json:String):UserPass = {
    CommonUtils._fromJson(json)
  }


  def fromUserPass(uid:String, pass:String):UserPass = {
    val passHash = AuthHelpers.sha512(pass)
    val hashStr = AuthHelpers.hash2Str(passHash)
    UserPass(uid, hashStr)
  }
}
