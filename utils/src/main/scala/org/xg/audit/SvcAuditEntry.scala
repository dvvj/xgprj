package org.xg.audit

import org.xg.user.UserType.UserType

case class SvcAuditEntry(userType: UserType, op:String, errMsg:String) {
  def getOpStr:String = s"$userType.$op"
}
