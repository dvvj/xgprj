package org.xg.alipayTest

import java.io.FileInputStream
import java.net.{URLDecoder, URLEncoder}
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime

import com.alipay.api.DefaultAlipayClient
import com.alipay.api.domain.AlipayTradeAppPayModel
import com.alipay.api.request.{AlipayOpenPublicInfoQueryRequest, AlipayOpenPublicMessageCustomSendRequest, AlipaySystemOauthTokenRequest, AlipayTradePagePayRequest}
import org.apache.commons.io.IOUtils
import org.xg.pay.{AlipayCfg, AlipayHelpers}

object Test1 extends App {
  val cfg = AlipayHelpers.testLocalCfg("/home/devvj/alipay-keys/rsa_private_key.raw")

  val dts = ZonedDateTime.now().toString
  AlipayHelpers.test1(cfg, dts, "test name", 1.0, "","")
}
