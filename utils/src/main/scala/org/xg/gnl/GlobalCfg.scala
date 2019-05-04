package org.xg.gnl

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.json4s.DefaultFormats

case class GlobalCfg(
  svcSvr:String,
  timeOutMs:Int,
  infoDbSvr:String,
  assetLocalPath:String
) {
  import GlobalCfg._
  def authCustomerURL:String = s"$svcSvr/$AUTH_CUSTOMER"
  def authProfURL:String = s"$svcSvr/$AUTH_MEDPROF"
  def authProfOrgAgentURL:String = s"$svcSvr/$AUTH_PROFORGAGENT"
  def authProfOrgURL:String = s"$svcSvr/$AUTH_PROFORG"
  def allCustomersURL:String = s"$svcSvr/$ALLCUSTOMERS"
  def customerProfilesURL:String = s"$svcSvr/$CUSTOMER_PROFILES"
  def customerProfsURL:String = s"$svcSvr/$CUSTOMER_PROFS"
  def customerByIdURL:String = s"$svcSvr/$FIND_CUSTOMER_BY_ID"
  def currOrdersURL:String = s"$svcSvr/$CURR_ORDERS"
  def placeOrderURL:String = s"$svcSvr/$PLACE_ORDER"
  def payOrderURL:String = s"$svcSvr/$PAY_ORDER"
  def cancelOrderURL:String = s"$svcSvr/$CANCEL_ORDER"
  def allProductsURL:String = s"$svcSvr/$ALLPRODUCTS"
  def imgAssetURL:String = s"$svcSvr/$IMAGE_ASSET"
  def pricePlanURL:String = s"$svcSvr/$PRICE_PLAN"
  def allPricePlansURL:String = s"$svcSvr/$ALL_PRICE_PLANS"
  def pricePlanAccessibleByURL:String = s"$svcSvr/$PRICE_PLAN_ACCESSIBLEBY"
  def rewardPlanURL:String = s"$svcSvr/$REWARD_PLAN"
  def rewardPlanAccessibleByURL:String = s"$svcSvr/$REWARD_PLAN_ACCESSIBLEBY"
  def customersRefedByURL:String = s"$svcSvr/$CUSTOMERS_REFED_BY"
  def refedCustomerOrdersURL:String = s"$svcSvr/$ORDERS_BY_REFED_CUSTOMERS"
  def medprofsOfURL:String = s"$svcSvr/$MEDPROFS_OF"
  def profOrgAgentsOfURL:String = s"$svcSvr/$PROFORG_AGENTS_OF"
  def orderStatsOfAgentURL:String = s"$svcSvr/$ORDERSTATS_OF_AGENT"
  def orderStatsOfOrgURL:String = s"$svcSvr/$ORDERSTATS_OF_ORG"
  def agentRewardPlansURL:String = s"$svcSvr/$AGENT_REWARD_PLANS"
  def orgRewardPlansURL:String = s"$svcSvr/$ORG_REWARD_PLANS"
  def addNewMedProfURL:String = s"$svcSvr/$ADD_NEW_MEDPROF"
  def addNewCustomerURL:String = s"$svcSvr/$ADD_NEW_CUSTOMER"
  def newProfileExistingCustomerURL:String = s"$svcSvr/$NEW_PROFILE_EXISTING_CUSTOMER"
  def existingCustomerProfileURL:String = s"$svcSvr/$EXISTING_CUSTOMER_PROFILES"
  def addPricePlanURL:String = s"$svcSvr/$ADD_PRICE_PLAN"
  def addRewardPlanURL:String = s"$svcSvr/$ADD_REWARD_PLAN"
  def updateCustomerPasswordURL: String = s"$svcSvr/$UPDATE_CUSTOMER_PASSWORD"
  def customerPricePlansURL:String = s"$svcSvr/$CUSTOMER_PRICEPLANS_OF_PROF"
  def alipayNotifyURL: String = s"$svcSvr/$ALIPAY_NOTIFY_URL"
  def alipayReturnURL: String = s"$svcSvr/$ALIPAY_RETURN_URL"
  def infoDbConnStr:String = s"$infoDbSvr/xgproj?user=dbuser&password=dbpass"
}

object GlobalCfg {
  private val AUTH_CUSTOMER = "auth/authCustomer"
  private val AUTH_MEDPROF = "auth/authMedProf"
  private val AUTH_PROFORGAGENT = "auth/authProfOrgAgent"
  private val AUTH_PROFORG = "auth/authProfOrg"
  private val ALLCUSTOMERS = "customer/testAll"
  private val CUSTOMER_PROFILES = "customer/profiles"
  private val CUSTOMER_PROFS = "customer/referringMedProfs"
  private val CURR_ORDERS = "order/currUser"
  private val PLACE_ORDER = "order/placeOrder"
  private val PAY_ORDER = "order/payOrder"
  private val CANCEL_ORDER = "order/cancelOrder"
  private val ALLPRODUCTS = "product/all"
  private val IMAGE_ASSET = "asset/img"
  private val PRICE_PLAN = "user/pricePlan"
  private val ALL_PRICE_PLANS = "user/allPricePlans"
  private val PRICE_PLAN_ACCESSIBLEBY = "user/pricePlanAccessibleBy"
  private val REWARD_PLAN = "user/rewardPlan"
  private val REWARD_PLAN_ACCESSIBLEBY = "user/rewardPlanAccessibleBy"
  private val ADD_REWARD_PLAN = "user/addRewardPlan"
  private val CUSTOMERS_REFED_BY = "prof/customers"
  private val ORDERS_BY_REFED_CUSTOMERS = "prof/refedCustomerOrders"
  private val MEDPROFS_OF = "profOrgAgent/profs"
  private val PROFORG_AGENTS_OF = "profOrg/agents"
  private val ORDERSTATS_OF_ORG = "profOrg/orderStats4Org"
  private val ORG_REWARD_PLANS = "profOrg/rewardPlans"
  private val AGENT_REWARD_PLANS = "profOrg/agentRewardPlans"
  private val ORDERSTATS_OF_AGENT = "profOrgAgent/orderStats"
  private val CUSTOMER_PRICEPLANS_OF_PROF = "prof/customerPricePlans"
  private val ADD_NEW_MEDPROF = "profOrgAgent/addNewMedProf"
  private val ADD_NEW_CUSTOMER = "prof/newCustomer"
  private val EXISTING_CUSTOMER_PROFILES = "prof/existingCustomerProfiles"
  private val NEW_PROFILE_EXISTING_CUSTOMER = "prof/newProfileExistingCustomer"
  private val FIND_CUSTOMER_BY_ID = "prof/findCustomerById"
  private val ADD_PRICE_PLAN = "user/addPricePlan"
  private val ALIPAY_NOTIFY_URL = "payment/alipayNotify"
  private val ALIPAY_RETURN_URL = "payment/alipayReturn"
  private val UPDATE_CUSTOMER_PASSWORD = "user/updateCustomerPassword"

  def fromJson(j:String):GlobalCfg = {
    import org.json4s.jackson.JsonMethods._
    implicit val fmt = DefaultFormats
    parse(j).extract[GlobalCfg]
  }

  val localTestCfg:GlobalCfg = {
    val strm = getClass.getResourceAsStream("/localTestCfg.json")
    val j = IOUtils.toString(strm, StandardCharsets.UTF_8)
    strm.close()
    fromJson(j)
  }

}