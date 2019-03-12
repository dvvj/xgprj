package org.xg.dbModels

import java.time.ZonedDateTime

import org.xg.gnl.DataUtils
import org.xg.gnl.DataUtils.utcTimeFromStr

case class MRewardPlanMap(
                      uid:String,
                      _planIds:String,
                      startTimeS:String,
                      expireTimes:Option[String]
                    ) {
  import DataUtils._
  private val planIds:Array[String] = _planIds.split(",").map(_.trim)
  def getPlanIds:Array[String] = planIds
  private val startTime:ZonedDateTime = utcTimeFromStr(startTimeS)
  def getStartTime:ZonedDateTime = startTime
  private val expireTime:Option[ZonedDateTime] = expireTimes.map(utcTimeFromStr)
  def getExpireTime:Option[ZonedDateTime] = expireTime
}

object MRewardPlanMap {
  import org.xg.gnl.DataUtils._
  def createJ(
               uid:String,
               planIds:String,
               startTime:String,
               expireTime:String
             ):MRewardPlanMap = {
    MRewardPlanMap(
      uid, planIds, startTime, noneIfNull(expireTime)
    )
  }
}