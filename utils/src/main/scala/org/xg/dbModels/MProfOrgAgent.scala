package org.xg.dbModels

import java.time.ZonedDateTime

import org.xg.json.CommonUtils

case class MProfOrgAgent(
  orgAgentId:String,
  orgId:String,
  name:String,
  info:String,
  phone:String,
  joinDate:String
) {

}

object MProfOrgAgent {
  def toJsons(agents:Array[MProfOrgAgent]):String = CommonUtils._toJsons(agents)
  def fromJsons(j:String):Array[MProfOrgAgent] = CommonUtils._fromJsons(j)
}