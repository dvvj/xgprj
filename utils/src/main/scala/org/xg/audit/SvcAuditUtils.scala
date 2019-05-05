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
  val MedProf_OrdersByrefedCustomer:SvcAuditEntry = medProfAudit("ordersByrefedCustomer")

  private def customerAudit(op:String):SvcAuditEntry =
    fromUserType(UserType.Customer, op)

  val Customer_TestAll:SvcAuditEntry = customerAudit("testAll")
  val Customer_GetProfiles:SvcAuditEntry = customerAudit("getProfiles")
  val Customer_ReferringMedProfs:SvcAuditEntry = customerAudit("referringMedProfs")
  val Customer_PlaceOrder:SvcAuditEntry = customerAudit("placeOrder")
  val Customer_CancelOrder:SvcAuditEntry = customerAudit("cancelOrder")
  val Customer_CurrentOrders:SvcAuditEntry = customerAudit("currOrders")

  val UserCfg_GetPricePlan:SvcAuditEntry = userCfgAudit("getPricePlan")
  val UserCfg_GetAllPricePlan:SvcAuditEntry = userCfgAudit("allPricePlans")
  val UserCfg_AllProducts:SvcAuditEntry = userCfgAudit("allProducts")
  val UserCfg_AddPricePlan:SvcAuditEntry = userCfgAudit("addPricePlan")
  val UserCfg_GetRewardPlan:SvcAuditEntry = userCfgAudit("getRewardPlan")
  val UserCfg_AddRewardPlan:SvcAuditEntry = userCfgAudit("addRewardPlan")

  private def profOrgAudit(op:String):SvcAuditEntry =
    fromUserType(UserType.MedProfOrg, op)

  val ProfOrg_GetAgents:SvcAuditEntry = profOrgAudit("getAgents")
  val ProfOrg_AgentRewardPlans:SvcAuditEntry = profOrgAudit("agentRewardPlans")
  val ProfOrg_RewardPlans:SvcAuditEntry = profOrgAudit("rewardPlans")
  val ProfOrg_OrderStats4Org:SvcAuditEntry = profOrgAudit("orderStats4Org")

}
