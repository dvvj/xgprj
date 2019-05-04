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

  private def medProfAudit(op:String):SvcAuditEntry =
    SvcAuditEntry(UserType.MedProf, op, s"$op error.")

  val MedProf_GetCustomers:SvcAuditEntry = medProfAudit("getCustomers")
  val MedProf_FindCustomerById:SvcAuditEntry = medProfAudit("findCustomerById")
  val MedProf_NewCustomer:SvcAuditEntry = medProfAudit("newCustomer")
  val MedProf_NewProfileExistingCustomer:SvcAuditEntry = medProfAudit("newProfileExistingCustomer")
  val MedProf_ExistingCustomerProfiles:SvcAuditEntry = medProfAudit("existingCustomerProfiles")
  val MedProf_GetCustomerPricePlans:SvcAuditEntry = medProfAudit("getCustomerPricePlans")
}
