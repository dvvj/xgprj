package org.xg;

import org.xg.auth.UserDbAuthority;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("payment")
public class PaymentOps {

  private final static Logger logger = Logger.getLogger(PaymentOps.class.getName());

  @POST
  @Path("alipayNotify")
//  @Consumes("application/x-www-form-urlencoded;text/html;charset=utf-8")
  @Consumes("application/x-www-form-urlencoded;charset=utf-8")
  public Response alipayNotify(String notifyContent) {
    logger.warning("=================================== alipayNotify:");
    logger.warning(notifyContent);
    return Response.ok().build();
  }

  @GET
  @Path("alipayReturn")
  @Produces(SvcUtils.MediaType_TXT_UTF8)
//  @Consumes("text/html;charset=utf-8")
  public Response alipayReturn(@QueryParam("out_trade_no") String outTradeNo) {
    logger.warning("=================================== alipayReturn:");
    logger.warning("outTradeNo: " + outTradeNo);
    return Response.ok(outTradeNo).build();
  }
}
