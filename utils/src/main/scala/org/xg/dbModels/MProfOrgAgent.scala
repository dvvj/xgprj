package org.xg.dbModels

import java.time.ZonedDateTime

case class MProfOrgAgent(
  orgAgentId:String,
  orgId:String,
  name:String,
  info:String,
  phone:String,
  joinDate:ZonedDateTime
) {

}
