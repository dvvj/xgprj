package org.xg;

import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.io.IOUtils;
import org.xg.audit.SvcAuditUtils;
import org.xg.auth.SvcHelpers;
import org.xg.gnl.GlobalCfg;
import org.xg.weixin.WxLoginReqMP;
import org.xg.weixin.WxMPPayReq;
import org.xg.weixin.WxPayReq;
import org.xg.weixin.WxUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Path("wxPay")
public class WxPayOps {

  private final static Logger logger = Logger.getLogger(WxPayOps.class.getName());

  private final static String wxKeyPath = SvcUtils.getCfg().wxKeyPath();
  private final static String _apiKeyPath = wxKeyPath + "/apikey.txt";
  private final static WxPayService wxPayService = WxUtils.createWxSvc(
    "wx6f58f5f5ff06f57f",
    "1409382102",
    _apiKeyPath,
    wxKeyPath + "/apiclient_cert.p12",
    false
  );


  @POST
  @Path("payReqMP")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response payReq(String wxPayReq) {
    return SvcUtils.tryOps(
      () -> {
        WxPayReq req = WxPayReq.fromJson(wxPayReq);

        WxMPPayReq mpReq = WxUtils.createOrder4MP(
          wxPayService,
          _apiKeyPath,
          req.amount(),
          req.info(),
          req.prodId(),
          "https://todo"
        );

        String s = WxMPPayReq.toJson(mpReq);
        logger.warning("======= mpReq: " + s);

        return Response.ok(s).build();
      },
      SvcAuditUtils.Weixin_PayReq()
    );

  }

  /**
   wx.login({
   success: function (res) {
   console.log("res: ", res)
   if (res.code) {
   wx.request({
   url: 'https://app.wonder4.life/webapi/wxPay/loginReq',
   data: {
   userId: 'todo',
   loginCode: res.code
   },
   method: "POST",
   header: {
   'content-type': 'application/json'
   },
   success: function (r) {
   console.log('r: ', r)
   console.log(r.data.openid)
   console.log(r.data.session_key)
   wx.request( {
   url: 'https://app.wonder4.life/webapi/wxPay/payReqMP',
   data: {
   userId: 'todo',
   prodId: 'prod001',
   amount: 1,
   info: 'todo-info'
   },
   method: "POST",
   header: {
   'content-type': 'application/json'
   },
   success: function (r2) {
   console.log('r2: ', r2)
   wx.requestPayment({
   'timeStamp': r2.data.timeStamp,
   'nonceStr': r2.data.nonceStr,
   'package': r2.data.package_,
   'signType': 'MD5',
   'paySign': r2.data.paySign,
   success: function (r3) {
   console.info('r3: ', r3)
   //报名
   //goApply(event, that)
   },
   fail: function (e3) {
   console.info(e3)
   },
   complete: function (c3) {
   console.info(c3)
   }
   })
   }
   }
   )
   },
   fail: function (r) {
   console.log('error getting openid, session_key', r)
   }
   })
   }
   else {
   console.log('failed to login', res)
   }
   },
   fail: function (r) {
   console.log('failed to login: ', r)
   }
   })

   */


  private static String readAppSecret(String path) {
    try {
      FileInputStream fin = new FileInputStream(path);
      String res = IOUtils.toString(fin, StandardCharsets.UTF_8);
      fin.close();
      return res.trim();
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final String _mpAppSecret = readAppSecret(wxKeyPath + "/mpAppSecret.txt");
  private static final String code2SessionTempl =
    "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

  @POST
  @Path("loginReq")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response loginReq(String wxLoginReq) {
    return SvcUtils.tryOps(
      () -> {
        logger.warning("======= wxLoginReq: " + wxLoginReq);

        WxLoginReqMP loginReq = WxLoginReqMP.fromJson(wxLoginReq);

        String code2SessionUrl = String.format(
          code2SessionTempl,
          "wxcce411c146c16195",
          _mpAppSecret,
          loginReq.loginCode()
        );

        int t = _mpAppSecret.charAt(_mpAppSecret.length()-1);

        logger.warning("======= secret: " + t);
        logger.warning("======= url: " + code2SessionUrl);
        String res = SvcHelpers.get(code2SessionUrl, "");

        logger.warning("======= code2Session response: " + res);

        return Response.ok(res).build();
      },
      SvcAuditUtils.Weixin_LoginReq()
    );

  }


  /**
   onLoad: function () {
     wx.login({
       success: function (res) {
         console.log("res: ", res)
         if (res.code) {
           wx.request({
             url: 'https://app.wonder4.life/webapi/wxPay/loginReq',
             data: {
               userId: 'todo',
               loginCode: res.code
             },
             method: "POST",
             header: {
               'content-type': 'application/json'
             },
             success: function (r) {
               console.log('r: ', r)
               console.log(r.data.openid)
               console.log(r.data.session_key)
             },
             fail: function (r) {
               console.log('error getting openid, session_key', r)
             }
           })
         }
         else {
           console.log('failed to login', res)
         }
       },
       fail: function (r) {
         console.log('failed to login: ', r)
       }
     })
   },
   */

  @POST
  @Path("weixinNotifyMP")
//  @Consumes("application/x-www-form-urlencoded;text/html;charset=utf-8")
  @Consumes(MediaType.TEXT_XML)
  public Response weixinNotify(String notifyContent) {
    logger.warning("=================================== weixinNotifyMP:");
    logger.warning(notifyContent);

    return SvcUtils.tryOps(
      () -> {
//        NotifyUtils.parseNotifyResultAndSave2Db(notifyContent, SvcUtils.getDbOps());
//        logger.info("Successfully processed notification: " + notifyContent);
        return Response.ok().build();
      },
      SvcAuditUtils.Weixin_NotifyMP()
    );
  }
}
