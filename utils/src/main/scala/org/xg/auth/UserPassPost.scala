package org.xg.auth

import java.util.Base64

import org.json4s.DefaultFormats

case class UserPassPost(uid:String, passHashBase64:String) {

}

object UserPassPost {
  def enc(uid:String, pass:Array[Byte]):UserPassPost = {
    UserPassPost(
      uid,
      Base64.getEncoder.encodeToString(pass)
    )
  }

  def toJson(upp:UserPassPost):String = {
    import org.json4s.jackson.Serialization._
    write(upp)(DefaultFormats)
  }

  def fromJson(json:String):UserPassPost = {
    import org.json4s.jackson.JsonMethods._
    implicit val _fmt = DefaultFormats
    parse(json).extract[UserPassPost]
  }
}