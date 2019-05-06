package org.xg.alipay

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

import org.xg.dbModels.TDbOps

object NotifyUtils {
  private def parseNotifyResult(urlEncoded:String):Map[String, String] = {
    val decoded = URLDecoder.decode(urlEncoded, StandardCharsets.UTF_8.name())

    val params = decoded.split("\\&")
    params.map { p =>
      val pp = p.split("=")
      val key = pp(0)
      val value = pp(1)
      key -> value
    }.toMap
  }

  def parseNotifyResultJ(urlEncoded:String):java.util.Map[String, String] = {
    import collection.JavaConverters._
    parseNotifyResult(urlEncoded).asJava
  }

  // out_trade_no=OutTradeNo10003-2019-04-08T22:44:12.27
  private val OutTradeParamName = "out_trade_no"
  private val OutTradeParamValuePfx = "OutTradeNo"
  def genOutTradeNo(orderId:Long, timeStr:String):String = s"$OutTradeParamValuePfx$orderId-$timeStr"
  def parseOutTraceOrderNo(otn:String):(Long, String) = {
    val parts = otn.split("-")
    val orderId = parts(0).substring(OutTradeParamValuePfx.length).toLong
    val dt = parts(1)
    orderId -> dt
  }
  def parseOutTraceOrderNo_OrderId(otn:String):Long = {
    parseOutTraceOrderNo(otn)._1
  }
  def parseNotifyResultAndSave2Db(urlEncoded:String, dbOps:TDbOps):Unit = {
    val params = parseNotifyResult(urlEncoded)

    if (params.contains(OutTradeParamName)) {
      try {
        val outTraceNo = params(OutTradeParamName)
        val parts = outTraceNo.split("-")
        val (orderId, dt) = parseOutTraceOrderNo(outTraceNo)
        dbOps.saveAlipayNotifyRawAndPayOrder(urlEncoded, orderId, dt)
      }
      catch {
        case t:Throwable => {
          t.printStackTrace()
          println(s"Failed to parse notify data: [$urlEncoded], save raw data only")
          dbOps.saveAlipayNotifyRawButDoNotPay(urlEncoded)
        }
      }
    }
    else {
      println(s"No [$OutTradeParamName] found: [$urlEncoded], save raw data only")
      dbOps.saveAlipayNotifyRawButDoNotPay(urlEncoded)
    }
  }

  def main(args:Array[String]):Unit = {
    val res = parseNotifyResult(
      //"gmt_create=2019-04-09+06%3A44%3A41&charset=UTF-8&gmt_payment=2019-04-09+06%3A45%3A18&notify_time=2019-04-09+06%3A45%3A19&subject=%E7%91%9E%E5%85%B8ACO+Gravid%E5%AD%95%E5%A6%87%E4%BA%A7%E5%A6%87%E5%A4%8D%E5%90%88%E7%BB%B4%E7%94%9F%E7%B4%A0&sign=DG3gAKA0KBtT6vWJC67NUuj30v11G50bftXONHJof4YHJcbtbZy2Q%2FM7FfpKye4t5Qlb1ik4GxnI3B9ck199qCHEW7vcFr4cvnguT9CProePMopYaSmboqYGA0VewFffmo2ESFv8fShwt7APjwJi%2Beq%2FVNCCTBGkj1%2Fh1isGzUkorMX54ZkpfDxBhviid6WviAk04u%2B%2BXEdrUp%2BgbzrJwRy0%2BW1p3TUpAOQqVo%2Fg8X9fIbT5Qb1I1lJdmDmYWdrPRqsxrVDokmTlfZDLpizlvktR%2FDdmySQgvzSrGDm%2Fl7huOWhKq5X2JSvhhPvZh99BSSE%2FTU9ANgUPD%2BaXq7%2BT9w%3D%3D&buyer_id=2088102177533674&body=%E7%91%9E%E5%85%B8ACO+Gravid%E5%AD%95%E5%A6%87%E4%BA%A7%E5%A6%87%E5%A4%8D%E5%90%88%E7%BB%B4%E7%94%9F%E7%B4%A0&invoice_amount=139.99&version=1.0&notify_id=2019040900222064518033671000086854&fund_bill_list=%5B%7B%22amount%22%3A%22139.99%22%2C%22fundChannel%22%3A%22ALIPAYACCOUNT%22%7D%5D&notify_type=trade_status_sync&out_trade_no=OutTradeNo10003-2019-04-08T22%3A44%3A12.27&total_amount=139.99&trade_status=TRADE_SUCCESS&trade_no=2019040922001433671000010794&auth_app_id=2016092700606319&receipt_amount=139.99&point_amount=0.00&app_id=2016092700606319&buyer_pay_amount=139.99&sign_type=RSA2&seller_id=2088102177468981"
      "charset=UTF-8&out_trade_no=OutTradeNo10003-2019-04-08T22%3A27%3A30.82&method=alipay.trade.page.pay.return&total_amount=9.99&sign=JWPZV%2Fa85QPTf87S5Tf6wwzc6d%2BZLtbA96cJG3pYZD27XYXqszYBU4KF4GJu7bL%2BjfUpYJBnKRH%2F15IEayziODmpbo7%2BDF1LY47LIttEpwzNffjM7EfakRDCDbE6kyo1wO3UM4tTUHCTGw3DSXmPf6ay9O4YLPvw%2FOmnfDTuE0Sb%2Bq%2F5KiWeVaAD%2FkzpHlg586stlCd7jTcXlmasRBSGHtXPimsd96UawtQRGsIVh%2Fhua1XLuSFxPoA3BCFM2Bfwb%2B4ezrcMSiOhwmYPbQXRuEW1dQuJF7niewzVEPpJeaZVV4DXrjoqLPYNuyfubRwIXEoMSmrqoQSil2xDcfrw6A%3D%3D&trade_no=2019040922001433671000013779&auth_app_id=2016092700606319&version=1.0&app_id=2016092700606319&sign_type=RSA2&seller_id=2088102177468981&timestamp=2019-04-09+06%3A29%3A06"
    )
    println(res.size)
  }

  val returnPageId:String = "c0ced014-142b-410b-bb6d-8bea90306fa1"
  val returnMsgTemplate:String =
    """
      |<!doctype html>
      |
      |<html lang="zh">
      |<head>
      |  <meta charset="utf-8">
      |  <title>付款成功</title>
      |</head>
      |
      |<body>
      |  <h5>付款成功 （共计：%s元），请关闭本窗口</h5>
      |  <div id="c0ced014-142b-410b-bb6d-8bea90306fa1" style="display: none;">%d</div>
      |</body>
      |</html>
    """.stripMargin
}
