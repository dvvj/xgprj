package org.xg.auth
import java.util

import org.xg.user.UserData.UserType.UserType

private class AuthorityBasicImpl(_userPassMap:Map[String, Array[Byte]]) extends TAuthority[String] {

  private val userPassMap:Map[String, Array[Byte]] =
    if (_userPassMap != null) _userPassMap else Map()
  private val userPassStrMap:Map[String, String] =
    userPassMap.mapValues(AuthHelpers.hash2Str)
  import AuthorityBasicImpl._
  def _authenticate(uid: String, passHash: Array[Byte]): Option[String] = {
    if (userPassMap.contains(uid)) {
      val sha = userPassMap(uid)
      //val userSha = AuthHelpers.sha512(pass)
      if (util.Arrays.equals(sha, passHash)) Option(TokenNotUsed)
      else None
    }
    else None
  }

  def _authenticateStr(uid: String, passHashStr: String): Option[String] = {
    if (userPassMap.contains(uid)) {
      val shaStr = userPassStrMap(uid)
      //val userSha = AuthHelpers.sha512(pass)
      if (shaStr == passHashStr) Option(TokenNotUsed)
      else None
    }
    else None
  }

  override def authenticate(uid: String, passHash: Array[Byte]): String = {
    val t = _authenticate(uid, passHash)

    if (t.nonEmpty) t.get
    else InvalidToken
  }

  override def authenticate(uid: String, passHashStr: String): String = {
    val t = _authenticateStr(uid, passHashStr)

    if (t.nonEmpty) t.get
    else InvalidToken
  }

  override def isValidToken(token: String): Boolean = {
    token != null && token != InvalidToken
  }
}

object AuthorityBasicImpl {
  private[auth] val TokenNotUsed = "TokenNotUsed"
  private[auth] val InvalidToken = ""

  def instance(userPassMap:Map[String, Array[Byte]]):TAuthority[String] = new AuthorityBasicImpl(userPassMap)
  def instanceJ(userPassMap:util.Map[String, Array[Byte]]):TAuthority[String] = {
    import collection.JavaConverters._
    new AuthorityBasicImpl(userPassMap.asScala.toMap)
  }

}