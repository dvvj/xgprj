package org.xg.dbModels

import java.time.ZonedDateTime

case class MProfOrg(
  orgId:String,
  name:String,
  info:String,
  orgNo:String,
  phone:String,
  joinDate:ZonedDateTime
) {

}
