package org.xg.auth

import org.xg.svc.AuthResult
import org.xg.user.UserData.UserType.UserType

trait TAuthority {
  //def authenticate(uid:String, pass: String, utype:UserType):Option[TToken]
  //def authenticate(uid:String, passHash: Array[Byte]):AuthResult
  def authenticate(uid:String, passHashStr: String):AuthResult

  //def isValidToken(token:String):Boolean
}
