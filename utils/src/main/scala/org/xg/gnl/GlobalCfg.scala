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
  def authURL:String = s"$svcSvr/$REQ_AUTH"
  def allCustomersURL:String = s"$svcSvr/$ALLCUSTOMERS"
  def currOrdersURL:String = s"$svcSvr/$CURR_ORDERS"
  def placeOrderURL:String = s"$svcSvr/$PLACE_ORDER"
  def payOrderURL:String = s"$svcSvr/$PAY_ORDER"
  def allProductsURL:String = s"$svcSvr/$ALLPRODUCTS"
  def imgAssetURL:String = s"$svcSvr/$IMAGE_ASSET"
  def pricePlanURL:String = s"$svcSvr/$PRICE_PLAN"
  def rewardPlanURL:String = s"$svcSvr/$REWARD_PLAN"
  def infoDbConnStr:String = s"$infoDbSvr/xgproj?user=dbuser&password=dbpass"
}

object GlobalCfg {
  private val REQ_AUTH = "auth/userPass"
  private val ALLCUSTOMERS = "customer/testAll"
  private val CURR_ORDERS = "order/testCurrUser"
  private val PLACE_ORDER = "order/placeOrder"
  private val PAY_ORDER = "order/payOrder"
  private val ALLPRODUCTS = "product/all"
  private val IMAGE_ASSET = "asset/img"
  private val PRICE_PLAN = "user/pricePlan"
  private val REWARD_PLAN = "user/rewardPlan"

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