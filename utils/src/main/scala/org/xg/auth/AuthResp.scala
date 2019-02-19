package org.xg.auth

import java.time.ZonedDateTime

import org.json4s.DefaultFormats
import org.xg.svc.UserPass

case class AuthResp(success:Boolean, token:String, msg:String)

object AuthResp {
  private[auth] def authSuccess(up:UserPass):AuthResp = {
    val timeStr = ZonedDateTime.now.toString
    val token = AuthHelpers.generateToken(
      Array(up.uid, timeStr,  up.passHashStr)
    )
    AuthResp(true, token, "")
  }

  private[auth] def authFailed(msg:String):AuthResp = AuthResp(false, "", msg)

  def fromJson(j:String):AuthResp = {
    import org.json4s.jackson.JsonMethods._
    implicit val fmt = DefaultFormats
    parse(j).extract[AuthResp]
  }

  def toJson(resp:AuthResp):String = {
    import org.json4s.jackson.Serialization._
    write(resp)(DefaultFormats)
  }
}
