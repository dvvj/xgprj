package org.xg;

import com.github.binarywang.wxpay.service.WxPayService;
import org.xg.audit.SvcAuditUtils;
import org.xg.gnl.GlobalCfg;
import org.xg.weixin.WxMPPayReq;
import org.xg.weixin.WxPayReq;
import org.xg.weixin.WxUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("wxPay")
public class WxPayOps {

  private final static Logger logger = Logger.getLogger(WxPayOps.class.getName());

  private final static String _apiKeyPath = "/home/devvj/.weixin/apikey.txt";
  private final static WxPayService wxPayService = WxUtils.createWxSvc(
    "wx6f58f5f5ff06f57f",
    "1409382102",
    _apiKeyPath,
    "/home/devvj/.weixin/apiclient_cert.p12",
    false
  );


  @POST
  @Path("payReq")
  @Consumes(MediaType.TEXT_PLAIN)
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
