package org.xg.pay

import com.alipay.api.DefaultAlipayClient
import com.alipay.api.domain.AlipayTradeAppPayModel
import com.alipay.api.request.AlipayTradePagePayRequest
import jdk.nashorn.internal.objects.Global
import org.apache.commons.io.IOUtils
import org.xg.alipay.NotifyUtils
import org.xg.gnl.DataUtils

object AlipayHelpers {

  private val APP_PRIVATE_KEY = """MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDpEFNBw1dWqn+7
                                  |66Qw1f97gdedioALrdFEKVx1RWCPfOVu+H2sPxAO5aNI6cZo7TjOJEx7Sc3kzuKH
                                  |svtfDzANKrl6oY69a8IQ2udwBshtLmdUqHuvf4zWP799V/dKIJIbShmTvN86UI6C
                                  |9nW6WbaZXuaZECjg9EhBke90io3P8skkWjQ3xUkbvZzV8ZJOEOb+8PNPM4g2mJxT
                                  |+Z1qYz04TR3egp50xOyaES0G+j+u1n0xKjQOKMOAhgRDNVYU4V4nSRbbAb9sXTkL
                                  |yPLn6LwXHtjlvJAX3uQdipJBHQ97VYX8jimUldBTRIg9PMa/syU0ZBjHF7kplVuC
                                  |C9hISijVAgMBAAECggEAeDbfevBTQHS5Zijpi/cVzJLwsGYcHeoOIuZitmqOn/SA
                                  |M3kg+ZOdqNFyI83RdZXG0y2N9ZngBcFgvzXmyuV+l4wOF971TWcjtqXy8qnM/+QL
                                  |kHNHOQLFlk1TbEfPWgDq3ACgqL+nNOEQYSTXHMPhl8KHlzPqof3et1Oa+7Xr5/IL
                                  |KbMdhSdMI/0B4MNTBFW55baTaWrAMIxqcwQ6TXdAV9qcPa9cvgPkwIz9mkqa61S6
                                  |worRKFVtBcCXiLifWwHwox0XXq8naqTMt4ae4VGs7Z6rOUA85sZHGlu7sDh/R7lY
                                  |wLEOHdf9SMzqOWrdUlnI98l3V8n9Vaeh7V03/s3JkQKBgQD0yCga7QfSPcwseZyv
                                  |Clj60isqAdXP3GsMiwrtpKqz8t6ZsnD1NnrkYgbjvOkcVLMqZxoIq6FpPI7NMUDa
                                  |DHpIMGkrm1CkOizaDYs4Gr0mQljLnmVbOtl35yrG5koXBkzOc77E/JvsLFwXY9dS
                                  |nogu+bXYixvn1pZGfpM/N/NilwKBgQDzvrBc5vtAMd/TMOIkbQBN3ykD6vXdtTth
                                  |7TuHMrsgrcQm3qUbArrRz4TFnT954ekduQbm/cmPrU9bG6W5WOEi1KGehSnGVXKQ
                                  |hL5sYA2jtdko76YKffCgw8HN/ZVE2kQd0ORdPbBwZjPBOiv/rUa+uMycQLL8jdES
                                  |P9mdH3D5cwKBgCLzybY+jlVY+aSIIPVlltsJ05wF8w/hO2/pEsBoOO/FCEAOGM8K
                                  |3nVqAe3W9bpT5dlyK2d9imjKeuGV3NrF2VghgYL2hIdOv0TLlkuGlee2pZM9pN9o
                                  |AdF7MsJ/86hh/+mi4WSjZWhN1UCJwx9K/8slYO/wvSVXoNGkFUZ7qMc1AoGBALHH
                                  |/yjzzIRpHEEU4Y0B9/vUbj0OTmB0hKiLeSTGatiOBYj4UeKenhbUTpo4dIj4rqhg
                                  |QVmP+EkUdtIUG09SNXvLLUrLA8pGnrYsLBwt/yTOSWNjUG9MposL3Tra39oFfTNm
                                  |HnqnuR5yXQNt45OsFfKcJZk+U46D/d9atOW2fhdjAoGAKWlOv7OmRiJurXWzQfl/
                                  |EyJhkAnU/vTnDzIBgCHkDMda3x2t8W+yog1Zs/ZNeS6//xDfirj6Em7L0FxEVb4f
                                  |3m/cZ9yoAi8TCJNG2VSzsAcosvJbgPG8/nbAVh5nQcVingfVtMCOOLK7zUWdQ+YJ
                                  |Vx+HA/xWMxpH4djtFyjetZo=
                                  |""".stripMargin

  def testLocalCfg():AlipayCfg = {
//    val strm = new FileInputStream(
//      privateKeyPath
//      //"/home/devvj/alipay-keys/rsa_private_key.raw"
//    )
    val privateKey = APP_PRIVATE_KEY  //IOUtils.toString(strm, StandardCharsets.UTF_8)

    val cfg = AlipayCfg(
      "https://openapi.alipaydev.com/gateway.do",
      "2016092700606319",
      privateKey,
      "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2DEj/nA0/6PJOi3BxFr8NbUf9dizBnrwffodpQ13VQZr1iOjmpc54jk/V6hic1CXvIsczzau306l6SlCztg4xmDlRnFY3/XXimJ+8Y7nrphSdWh4cwGRRTWXTINdCjmbtUY4lk6ifl7jdmE1yc/5fcd5CvEcUyQjyYqxzqtQOpOJqEAaCm/b69XJdXnZxPzARYtaZf3Z8Xnatw4Kgy2EtQcIFKGmkfcu+IKduE0KdARm1tfO6w6TbUKP2edcunfcoJRdPN/DJDjC7ucma9p9HkCZ0YlzsDLK2qLjp7jbMjFhZt1IwfhDJTN3m+Q95XbGfn2r1R5zfjq2gfYggW4KtQIDAQAB",
      "RSA2"
    )
    cfg
  }

  def test1RandTraceNo(cfg:AlipayCfg, orderId:Long, prodName:String, totalAmount:Double, returnUrl:String, notifyUrl:String):String = {
    test1(
      cfg,
      NotifyUtils.genOutTradeNo(orderId, DataUtils.utcTimeNowStr),
      //LocalDateTime.now().toString,
      prodName,
      totalAmount,
      returnUrl,
      notifyUrl
    )
  }

  def test1(
             cfg:AlipayCfg,
             outTradeNo:String,
             prodName:String,
             totalAmount:Double,
             returnUrl:String,
             notifyUrl:String
           ):String = {

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
    req.setReturnUrl(returnUrl)
    req.setNotifyUrl(notifyUrl)
    val currentTime = System.currentTimeMillis()
    val payModel = new AlipayTradeAppPayModel
    payModel.setSubject(prodName)
    payModel.setBody(prodName)
    payModel.setProductCode("FAST_INSTANT_TRADE_PAY")
    payModel.setTotalAmount(f"$totalAmount%.2f")
    payModel.setOutTradeNo(outTradeNo)
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

    //val resp = client.pageExecute(req).getBody
    val resp = client.pageExecute(req)

    val content = resp.getBody //URLDecoder.decode(resp.getBody, "UTF-8")
    println(content)
    content
  }
}
