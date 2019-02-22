package org.xg.auth

import java.time.{ZoneId, ZoneOffset, ZonedDateTime}

import org.xg.gnl.{DataUtils, SvcResp}

class SessionMgr(val timeOutSec:Int) {

  import SessionMgr._
  import collection.mutable
  private var _sessMap = mutable.Map[String, UserSession]()
  private var _revMap = mutable.Map[String, UserSession]()

  private def _addSession(uid:String, time:ZonedDateTime, token:String):Unit = {
    val expireTime = time.plusSeconds(new java.lang.Long(timeOutSec))
    val sess = UserSession(uid, expireTime, token)
    _sessMap += uid -> sess
    _revMap += token -> sess
  }

  private def _removeSession(uid:String):Unit = {
    _sessMap.remove(uid)
  }

  import SvcResp._
  def addSession(uid:String, token:String):SvcResp = {
//    if (!_sessMap.contains(uid)) {
//      synchronized {
//        if (!_sessMap.contains(uid)) {
//          val zdt = DataUtils.utcTimeNow
//          //println(zdt)
//          _addSession(uid, zdt, token)
//          SUCCESS
//        }
//        else {
//          error(s"Uid [$uid]: One session per user only!")
//        }
//      }
//    }
//    else {
//      error(s"Uid [$uid]: One session per user only!")
//    }
    synchronized {
      val zdt = DataUtils.utcTimeNow
      _addSession(uid, zdt, token)
      SUCCESS
    }
  }

  def checkSession(uid:String, token:String):SvcResp = {

    if (_sessMap.contains(uid)) {
      val sess = _sessMap(uid)

      val timeNow = DataUtils.utcTimeNow

      if (timeNow.compareTo(sess.expireTime) > 0) {
        synchronized {
          _removeSession(uid)
        }
        error(s"Uid [$uid]: session expired!")
      }
      else {
        if (sess.token == token)
          SUCCESS
        else
          error(s"Uid [$uid]: token mismatch (a new session created?!)")
      }
    }
    else {
      error(s"Uid [$uid]: no session!")
    }
  }

  def reverseLookup(token:String):String = {
    if (_revMap.contains(token)) {
      _revMap(token).uid
    }
    else InvalidUid
  }

  private[auth] def testAllUsers():Set[String] = {
    _sessMap.keySet.toSet
  }
}

object SessionMgr {
  val InvalidUid:String = ""
  def create(timeOutSec:Int):SessionMgr = new SessionMgr(timeOutSec)
}
