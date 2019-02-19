package org.xg.auth

import java.time.{ZoneId, ZoneOffset, ZonedDateTime}

import org.xg.gnl.SvcResp

class SessionMgr(val timeOutSec:Int) {

  import collection.mutable
  private var _sessMap = mutable.Map[String, UserSession]()

  private def _addSession(uid:String, time:ZonedDateTime, token:String):Unit = {
    val expireTime = time.plusSeconds(new java.lang.Long(timeOutSec))
    _sessMap += uid -> UserSession(uid, expireTime, token)
  }

  private def _removeSession(uid:String):Unit = {
    _sessMap.remove(uid)
  }

  import SvcResp._
  def addSession(uid:String, token:String):SvcResp = {
    if (!_sessMap.contains(uid)) {
      synchronized {
        if (!_sessMap.contains(uid)) {
          val zdt = ZonedDateTime.now(ZoneId.of("UTC"))
          //println(zdt)
          _addSession(uid, zdt, token)
          SUCCESS
        }
        else {
          error(s"Uid [$uid]: One session per user only!")
        }
      }
    }
    else {
      error(s"Uid [$uid]: One session per user only!")
    }
  }

  def checkSession(uid:String):SvcResp = {

    if (_sessMap.contains(uid)) {
      val sess = _sessMap(uid)

      val timeNow = ZonedDateTime.now(ZoneId.of("UTC"))

      if (timeNow.compareTo(sess.expireTime) > 0) {
        synchronized {
          _removeSession(uid)
        }
        error(s"Uid [$uid]: session expired!")
      }
      else
        SUCCESS
    }
    else {
      error(s"Uid [$uid]: no session!")
    }
  }

  private[auth] def testAllUsers():Set[String] = {
    _sessMap.keySet.toSet
  }
}

object SessionMgr {
  def create(timeOutSec:Int):SessionMgr = new SessionMgr(timeOutSec)
}
