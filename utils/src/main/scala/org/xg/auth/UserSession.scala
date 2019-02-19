package org.xg.auth

import java.time.ZonedDateTime

case class UserSession(uid:String, expireTime:ZonedDateTime, token:String)
