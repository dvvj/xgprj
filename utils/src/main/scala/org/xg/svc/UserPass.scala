package org.xg.svc

import org.json4s.DefaultFormats
import org.xg.auth.AuthHelpers

case class UserPass(uid:String, passHashStr:String)

object UserPass {
  //  def enc(uid:String, pass:Array[Byte]):UserPassBase64 = {
  //    UserPassBase64(
  //      uid,
  //      Base64.getEncoder.encodeToString(pass)
  //    )
  //  }

  def toJson(upp:UserPass):String = {
    import org.json4s.jackson.Serialization._
    write(upp)(DefaultFormats)
  }

  def fromJson(json:String):UserPass = {
    import org.json4s.jackson.JsonMethods._
    implicit val _fmt = DefaultFormats
    parse(json).extract[UserPass]
  }


  def fromUserPass(uid:String, pass:String):UserPass = {
    val passHash = AuthHelpers.sha512(pass)
    val hashStr = AuthHelpers.hash2Str(passHash)
    UserPass(uid, hashStr)
  }
}
