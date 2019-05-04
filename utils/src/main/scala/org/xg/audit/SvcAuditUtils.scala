package org.xg.audit

import org.xg.user.UserType
import org.xg.user.UserType.UserType

object SvcAuditUtils {

  private def profOrgAgentAudit(op:String):SvcAuditEntry =
    SvcAuditEntry(UserType.MedProfOrgAgent, op, s"$op error.")

  val ProfOrgAgent_GetProfs:SvcAuditEntry = profOrgAgentAudit("getProfs")
  val ProfOrgAgent_AddNewMedProf:SvcAuditEntry = profOrgAgentAudit("addNewMedProf")
  val ProfOrgAgent_GetOrderStatsOf:SvcAuditEntry = profOrgAgentAudit("getOrderStatsOf")
  //val ProfOrgAgentGetProfs:SvcAuditEntry = profOrgAgentAudit("getProfs")

}
