package org.xg.alipayTest

import java.io.FileInputStream
import java.net.{URLDecoder, URLEncoder}
import java.nio.charset.StandardCharsets

import com.alipay.api.DefaultAlipayClient
import com.alipay.api.domain.AlipayTradeAppPayModel
import com.alipay.api.request.{AlipayOpenPublicInfoQueryRequest, AlipayOpenPublicMessageCustomSendRequest, AlipaySystemOauthTokenRequest, AlipayTradePagePayRequest}
import org.apache.commons.io.IOUtils

object Test1 extends App {

  val strm = new FileInputStream("/home/devvj/alipay-keys/rsa_private_key.raw")
  val privateKey = IOUtils.toString(strm, StandardCharsets.UTF_8)
  val cfg = AlipayCfg(
    "https://openapi.alipaydev.com/gateway.do",
    "2016092700606319",
    privateKey,
    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2DEj/nA0/6PJOi3BxFr8NbUf9dizBnrwffodpQ13VQZr1iOjmpc54jk/V6hic1CXvIsczzau306l6SlCztg4xmDlRnFY3/XXimJ+8Y7nrphSdWh4cwGRRTWXTINdCjmbtUY4lk6ifl7jdmE1yc/5fcd5CvEcUyQjyYqxzqtQOpOJqEAaCm/b69XJdXnZxPzARYtaZf3Z8Xnatw4Kgy2EtQcIFKGmkfcu+IKduE0KdARm1tfO6w6TbUKP2edcunfcoJRdPN/DJDjC7ucma9p9HkCZ0YlzsDLK2qLjp7jbMjFhZt1IwfhDJTN3m+Q95XbGfn2r1R5zfjq2gfYggW4KtQIDAQAB",
    "RSA2"
  )
  val client = new DefaultAlipayClient(
    cfg.url,
    cfg.appId,
    cfg.appPrivateKey,
    "json",
    "UTF-8",
    cfg.alipayPublicKey,
    cfg.signType
  )

//  val openPubInfoRequest = new AlipayOpenPublicInfoQueryRequest();
//  val resp = client.execute(openPubInfoRequest)
//  println(resp.getBody)

  val req = new AlipayTradePagePayRequest
  req.setReturnUrl("https://reqres.in//api/users?page=2")
  req.setNotifyUrl("https://reqres.in//api/users?page=2")
  val currentTime = System.currentTimeMillis()
  val payModel = new AlipayTradeAppPayModel
  payModel.setSubject("标题")
  //payModel.setProductCode()
  payModel.setTotalAmount("0.01")
  payModel.setOutTradeNo("fdsafdasfdsaf14343fs1fsda12321321312")
  req.setBizModel(payModel);//填充业务参数
  //  req.setBizContent("{" +
//    "    \"out_trade_no\":" +currentTime+","+
//    "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
//    "    \"total_amount\":100.00," +
//    "    \"subject\":\"大乐透\"," +
//    "    \"body\":\"大乐透\"," +
//    "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
//    "    \"extend_params\":{" +
//    "    \"sys_service_provider_id\":\"2088212752111244\"" +
//    "    }"+
//    "  }");//填充业务参数

//  val req = new AlipayOpenPublicMessageCustomSendRequest()
//  val uid = "aYMvrMC8+qdi3Mj1lqxRZJPUsrychFTewHXFVXq5ySDxWgIluiZN3K2r70Eebm4r01"
//  val msg = "{'articles':[{'action_name':'立即查看','desc':'这是图文内容','image_url':'http://pic.alipayobjects.com/e/201311/1PaQ27Go6H_src.jpg','title':'这是标题','url':'https://www.alipay.com/'}],'msg_type':'image-text','to_user_id':'" + uid + "'}"

  val resp = client.pageExecute(req).getBody
  val content = resp //URLDecoder.decode(resp.getBody, "UTF-8")
  println(content)
}