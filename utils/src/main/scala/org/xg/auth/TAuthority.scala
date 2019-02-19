package org.xg.auth

import org.xg.user.UserData.UserType.UserType

trait TAuthority[TToken] {
  //def authenticate(uid:String, pass: String, utype:UserType):Option[TToken]
  def authenticate(uid:String, passHash: Array[Byte]):TToken
  def authenticate(uid:String, passHashStr: String):TToken

  def isValidToken(token:TToken):Boolean
}
