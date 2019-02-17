package org.xg.auth

import java.util.Base64

import org.json4s.DefaultFormats

case class UserPass(uid:String, passHash:Array[Byte])

case class UserPassBase64(uid:String, passHashBase64:String)

object UserPassBase64 {
  def enc(uid:String, pass:Array[Byte]):UserPassBase64 = {
    UserPassBase64(
      uid,
      Base64.getEncoder.encodeToString(pass)
    )
  }

  def toJson(upp:UserPassBase64):String = {
    import org.json4s.jackson.Serialization._
    write(upp)(DefaultFormats)
  }

  def fromJson(json:String):UserPassBase64 = {
    import org.json4s.jackson.JsonMethods._
    implicit val _fmt = DefaultFormats
    parse(json).extract[UserPassBase64]
  }

  def decFromJson(json:String):UserPass = {
    val upBase64 = fromJson(json)
    UserPass(
      upBase64.uid,
      Base64.getDecoder.decode(upBase64.passHashBase64)
    )
  }
}