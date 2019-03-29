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
  def authOrgURL:String = s"$svcSvr/$AUTH_PROFORG"
  def allCustomersURL:String = s"$svcSvr/$ALLCUSTOMERS"
  def currOrdersURL:String = s"$svcSvr/$CURR_ORDERS"
  def placeOrderURL:String = s"$svcSvr/$PLACE_ORDER"
  def payOrderURL:String = s"$svcSvr/$PAY_ORDER"
  def allProductsURL:String = s"$svcSvr/$ALLPRODUCTS"
  def imgAssetURL:String = s"$svcSvr/$IMAGE_ASSET"
  def pricePlanURL:String = s"$svcSvr/$PRICE_PLAN"
  def rewardPlanURL:String = s"$svcSvr/$REWARD_PLAN"
  def customersRefedByURL:String = s"$svcSvr/$CUSTOMERS_REFED_BY"
  def refedCustomerOrdersURL:String = s"$svcSvr/$ORDERS_BY_REFED_CUSTOMERS"
  def medprofsOfURL:String = s"$svcSvr/$MEDPROFS_OF"
  def orderStatsOfURL:String = s"$svcSvr/$ORDERSTATS_OF"
  def addNewMedProfURL:String = s"$svcSvr/$ADD_NEW_MEDPROF"
  def addNewCustomerURL:String = s"$svcSvr/$ADD_NEW_CUSTOMER"
  def infoDbConnStr:String = s"$infoDbSvr/xgproj?user=dbuser&password=dbpass"
}

object GlobalCfg {
  private val AUTH_CUSTOMER = "auth/customerPass"
  private val AUTH_MEDPROF = "auth/profPass"
  private val AUTH_PROFORG = "auth/orgsPass"
  private val ALLCUSTOMERS = "customer/testAll"
  private val CURR_ORDERS = "order/currUser"
  private val PLACE_ORDER = "order/placeOrder"
  private val PAY_ORDER = "order/payOrder"
  private val ALLPRODUCTS = "product/all"
  private val IMAGE_ASSET = "asset/img"
  private val PRICE_PLAN = "user/pricePlan"
  private val REWARD_PLAN = "user/rewardPlan"
  private val CUSTOMERS_REFED_BY = "prof/customers"
  private val ORDERS_BY_REFED_CUSTOMERS = "order/refedCustomerOrders"
  private val MEDPROFS_OF = "profOrg/profs"
  private val ORDERSTATS_OF = "profOrg/orderStats"
  private val ADD_NEW_MEDPROF = "profOrg/addNewMedProf"
  private val ADD_NEW_CUSTOMER = "prof/newCustomer"

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