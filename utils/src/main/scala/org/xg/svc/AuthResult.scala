package org.xg.svc

case class AuthResult(result:Boolean, msg:String)

object AuthResult {
  val success:AuthResult = AuthResult(true, null)

  def failure(msg:String):AuthResult = AuthResult(false, msg)
}
