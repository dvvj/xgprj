package org.xg;

import com.github.binarywang.wxpay.service.WxPayService;
import org.xg.gnl.GlobalCfg;
import org.xg.weixin.WxPayReq;
import org.xg.weixin.WxUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("wxPay")
public class WxPayOps {


  private final static WxPayService wxPayService = WxUtils.createWxSvc(
    "wx6f58f5f5ff06f57f",
    "1409382102",
    "/home/devvj/.weixin/apikey.txt",
    "/home/devvj/.weixin/apiclient_cert.p12",
    false
  );


//  @POST
//  @Path("payReq")
//  @Consumes(MediaType.TEXT_PLAIN)
//  public Response payReq(String wxPayReq) {
//    WxPayReq req = WxPayReq.fromJson(wxPayReq);
//
//    WxUtils.createOrder(
//      wxPayService,
//      req.amount(),
//      req.info(),
//      req.prodId(),
//      "https://todo",
//
//      )
//  }

}
