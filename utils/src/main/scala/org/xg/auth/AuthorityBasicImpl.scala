package org.xg.auth
import java.util

import org.xg.user.UserData.UserType.UserType

private class AuthorityBasicImpl(_userPassMap:Map[String, Array[Byte]]) extends TAuthority[String] {

  private val userPassMap:Map[String, Array[Byte]] =
    if (_userPassMap != null) _userPassMap else Map()
  import AuthorityBasicImpl._
  override def authenticate(uid: String, pass: String, utype: UserType): Option[String] = {
    if (userPassMap.contains(uid)) {
      val sha = userPassMap(uid)
      val userSha = AuthHelpers.sha512(pass)
      if (util.Arrays.equals(sha, userSha)) Option(TokenNotUsed)
      else None
    }
    else None
  }


}

object AuthorityBasicImpl {
  private[auth] val TokenNotUsed = "TokenNotUsed"

  def instance(userPassMap:Map[String, Array[Byte]]):TAuthority[String] = new AuthorityBasicImpl(userPassMap)
}