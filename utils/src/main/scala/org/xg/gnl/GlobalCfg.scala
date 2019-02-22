package org.xg.gnl

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.json4s.DefaultFormats

case class GlobalCfg(
  svcSvr:String
) {
  import GlobalCfg._
  def authURL:String = s"$svcSvr/$REQ_AUTH"
  def allCustomersURL:String = s"$svcSvr/$ALLCUSTOMERS"
  def currOrdersURL:String = s"$svcSvr/$CURR_ORDERS"
  def placeOrderURL:String = s"$svcSvr/$PLACE_ORDER"
  def allProductsURL:String = s"$svcSvr/$ALLPRODUCTS"
}

object GlobalCfg {
  private val REQ_AUTH = "auth/userPass"
  private val ALLCUSTOMERS = "customer/testAll"
  private val CURR_ORDERS = "order/testCurrUser"
  private val PLACE_ORDER = "order/placeOrder"
  private val ALLPRODUCTS = "product/all"

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