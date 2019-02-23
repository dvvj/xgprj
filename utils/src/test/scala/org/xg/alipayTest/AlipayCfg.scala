package org.xg.alipayTest

// AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
case class AlipayCfg(
  url:String,
  appId:String,
  appPrivateKey:String,
  alipayPublicKey:String,
  signType:String
)