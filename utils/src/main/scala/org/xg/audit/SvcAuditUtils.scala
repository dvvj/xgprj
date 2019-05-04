package org.xg.audit

import org.xg.user.UserType
import org.xg.user.UserType.UserType

object SvcAuditUtils {

  import SvcAuditEntry._
  private def profOrgAgentAudit(op:String):SvcAuditEntry =
    fromUserType(UserType.MedProfOrgAgent, op)

  val ProfOrgAgent_GetProfs:SvcAuditEntry = profOrgAgentAudit("getProfs")
  val ProfOrgAgent_AddNewMedProf:SvcAuditEntry = profOrgAgentAudit("addNewMedProf")
  val ProfOrgAgent_GetOrderStatsOf:SvcAuditEntry = profOrgAgentAudit("getOrderStatsOf")
  //val ProfOrgAgentGetProfs:SvcAuditEntry = profOrgAgentAudit("getProfs")

  private def medProfAudit(op:String):SvcAuditEntry =
    fromUserType(UserType.MedProf, op)

  val MedProf_GetCustomers:SvcAuditEntry = medProfAudit("getCustomers")
  val MedProf_FindCustomerById:SvcAuditEntry = medProfAudit("findCustomerById")
  val MedProf_NewCustomer:SvcAuditEntry = medProfAudit("newCustomer")
  val MedProf_NewProfileExistingCustomer:SvcAuditEntry = medProfAudit("newProfileExistingCustomer")
  val MedProf_ExistingCustomerProfiles:SvcAuditEntry = medProfAudit("existingCustomerProfiles")
  val MedProf_GetCustomerPricePlans:SvcAuditEntry = medProfAudit("getCustomerPricePlans")

  private def customerAudit(op:String):SvcAuditEntry =
    fromUserType(UserType.Customer, op)

  val Customer_TestAll:SvcAuditEntry = medProfAudit("testAll")
  val Customer_GetProfiles:SvcAuditEntry = medProfAudit("getProfiles")
  val Customer_ReferringMedProfs:SvcAuditEntry = medProfAudit("referringMedProfs")

  val UserCfg_GetPricePlan:SvcAuditEntry = userCfgAudit("getPricePlan")
  val UserCfg_GetAllPricePlan:SvcAuditEntry = userCfgAudit("allPricePlans")

}
