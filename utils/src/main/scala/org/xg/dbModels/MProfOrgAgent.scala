package org.xg.dbModels

import java.time.ZonedDateTime

case class MProfOrgAgent(
  orgAgentId:String,
  name:String,
  info:String,
  orgNo:String,
  phone:String,
  joinDate:ZonedDateTime
) {

}
