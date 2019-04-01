package org.xg.dbModels

import java.time.ZonedDateTime

case class MProfOrgAgent(
  orgAgentId:String,
  name:String,
  info:String,
  phone:String,
  joinDate:ZonedDateTime
) {

}
