package org.xg.audit

import org.xg.user.UserType.UserType

case class SvcAuditEntry(auditType: String, op:String, errMsg:String) {
  def getOpStr:String = s"$auditType.$op"
}

object SvcAuditEntry {
  def fromUserType(userType: UserType, op:String):SvcAuditEntry =
    SvcAuditEntry(userType.toString, op, s"$op error.")

  private val userCfg = "UserCfg"
  def userCfgAudit(op:String):SvcAuditEntry = SvcAuditEntry(userCfg, op, s"$op error.")

  private val alipay = "Alipay"
  def alipayAudit(op:String):SvcAuditEntry = SvcAuditEntry(alipay, op, s"$op error.")

  private val WxPay = "WxPay"
  def wxPayAudit(op:String):SvcAuditEntry = SvcAuditEntry(WxPay, op, s"$op error.")
}