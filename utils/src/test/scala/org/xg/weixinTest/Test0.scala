package org.xg.weixinTest

import com.github.binarywang.wxpay.config.WxPayConfig
import com.github.binarywang.wxpay.service.WxPayService
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl
import org.apache.commons.lang3.StringUtils
import org.xg.weixin.WxUtils

object Test0 {

  private val apiKeyPath = "/home/devvj/.weixin/apikey.txt";
  private val wxService: WxPayService = WxUtils.createWxSvc(
    "wx6f58f5f5ff06f57f",
    "1409382102",
    "/home/devvj/.weixin/apikey.txt",
    "/home/devvj/.weixin/apiclient_cert.p12",
    false
  )

  def main(args:Array[String]):Unit = {

//    val order = WxUtils.createOrder(
//      wxService,
//      1,
//      "zhuti",
//      "prodId001",
//      "http://todo/notifyUrl",
//      200
//    )
//
//    println(order)

    WxUtils.createOrder4MP(wxService, apiKeyPath, 1, "test body", "001", "http://todo")
  }
}
