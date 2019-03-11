package org.xg.dbModels

case class MPricePlanMap(
  uid:String,
  _planIds:String,
  startTimeS:String,
  expireTimes:Option[String]
) {
  private val planIds:Array[String] = _planIds.split(",").map(_.trim)
  def getPlanIds:Array[String] = planIds
}

object MPricePlanMap {
  import org.xg.gnl.DataUtils._
  def createJ(
    uid:String,
    planIds:String,
    startTime:String,
    expireTime:String
  ):MPricePlanMap = {
    MPricePlanMap(
      uid, planIds, startTime, noneIfNull(expireTime)
    )
  }
}