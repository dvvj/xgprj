package org.xg.weixin

import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult
import com.github.binarywang.wxpay.bean.request
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult
import com.github.binarywang.wxpay.config.WxPayConfig
import com.github.binarywang.wxpay.constant.WxPayConstants
import com.github.binarywang.wxpay.service.WxPayService
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl
import com.google.zxing.{BarcodeFormat, MultiFormatWriter}
import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.common.BitMatrix
import javafx.scene.image.Image
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils

object WxUtils {

  private val _dateTimeFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
  private val _defaultZoneId = ZoneId.of("+8")

  def getApiKey(keyFilePath:String):String = {
    val strm = new FileInputStream(keyFilePath)
    val res = IOUtils.toString(strm, StandardCharsets.US_ASCII)
    strm.close()
    res.trim
  }

  def createWxSvc(
                 appId:String,
                 mchId:String,
                 mchKeyPath:String,
                 keyPath:String,
                 useSandbox:Boolean
                 ):WxPayService = {
    val payConfig = new WxPayConfig
    payConfig.setAppId(StringUtils.trimToNull(appId))
    payConfig.setMchId(StringUtils.trimToNull(mchId))
    payConfig.setMchKey(StringUtils.trimToNull(getApiKey(mchKeyPath)))
    //    payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getSubAppId))
    //    payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getSubMchId))
    payConfig.setKeyPath(keyPath)
    // 可以指定是否使用沙箱环境
    payConfig.setUseSandboxEnv(useSandbox)
    val wxPayService = new WxPayServiceImpl
    wxPayService.setConfig(payConfig)
    wxPayService
  }

  def createOrder(
                   wxService:WxPayService,
                   amount:Integer,
//                   mchId:String,
//                   appId:String,
                   body:String,
                   prodId:String,
                   notifyUrl:String,
                   qrImgSize:Integer
                 ):Array[Byte] = {
    val dtNow = ZonedDateTime.now(_defaultZoneId)
    val nowStr = dtNow.format(_dateTimeFormat)
    val dtExp = dtNow.plusHours(1)
    val expStr = dtExp.format(_dateTimeFormat)
    val outTradeNo = "OutTradeNo_" + nowStr

    val req = WxPayUnifiedOrderRequest.newBuilder()
      .body(body)
      .productId(prodId)
      .outTradeNo(outTradeNo)
      .timeStart(nowStr)
      .timeExpire(expStr)
      .totalFee(amount)
      .tradeType(WxPayConstants.TradeType.NATIVE)
      .spbillCreateIp("123.123.123.123")
      .notifyUrl(notifyUrl)
      .build()
//    req.setMchId(mchId)
//    req.setAppid(appId)
    //req.setSignType("MD5")
    val t = wxService.createOrder[WxPayNativeOrderResult](req)
    val bc = wxService.createScanPayQrcodeMode2(t.getCodeUrl, null, qrImgSize)
    bc
  }

  def createOrder4MP(
    wxService:WxPayService,
    apiKeyPath:String,
    openid:String,
    amount:Integer,
    //                   mchId:String,
    //                   appId:String,
    body:String,
    prodId:String,
    notifyUrl:String
  ):WxMPPayReq = {
    val dtNow = ZonedDateTime.now(_defaultZoneId)
    val nowStr = dtNow.format(_dateTimeFormat)
    val dtExp = dtNow.plusHours(1)
    val expStr = dtExp.format(_dateTimeFormat)
    val outTradeNo = "OutTradeNo_" + nowStr

    val req = WxPayUnifiedOrderRequest.newBuilder()
      .body(body)
      .productId(prodId)
      .outTradeNo(outTradeNo)
      .timeStart(nowStr)
      .timeExpire(expStr)
      .totalFee(amount)
      .openid(openid)
      .tradeType(WxPayConstants.TradeType.JSAPI)
      .spbillCreateIp("123.123.123.123")
      .notifyUrl(notifyUrl)
      .build()
    //    req.setMchId(mchId)
    //    req.setAppid(appId)
    //req.setSignType("MD5")
    val t = wxService.unifiedOrder(req)

    val prepayId = t.getPrepayId
    val pkg = s"prepay_id=$prepayId"
    val ts = ZonedDateTime.now().toInstant.toEpochMilli
    val paySign = getPaySign(
      List(
        "appId" -> t.getAppid,
        "nonceStr" -> t.getNonceStr,
        "package" -> pkg,
        "signType" -> "MD5",
        "timeStamp" -> ts.toString,
        "key" -> getApiKey(apiKeyPath)
      )
    )
    //println(paySign)
    WxMPPayReq(
      ts.toString,
      t.getNonceStr,
      pkg,
      "MD5",
      paySign
    )
  }

  def getPaySign(tuples:List[(String, String)]):String = {
    val sin = tuples.map { p =>
      val (k, v) = p
      s"$k=$v"
    }.mkString("&")
    val res = DigestUtils.md5Hex(sin).toUpperCase()
    println(s"sin: $sin, res: $res")
    res
  }
}
