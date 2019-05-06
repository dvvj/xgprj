package org.xg;

import org.xg.alipay.NotifyUtils;
import org.xg.audit.SvcAuditUtils;
import org.xg.auth.UserDbAuthority;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
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

    return SvcUtils.tryOps(
      () -> {
        NotifyUtils.parseNotifyResultAndSave2Db(notifyContent, SvcUtils.getDbOps());
        logger.info("Successfully processed notification: " + notifyContent);
        return Response.ok().build();
      },
      SvcAuditUtils.Alipay_Notify()
    );
  }

  @GET
  @Path("alipayReturn")
  @Produces(SvcUtils.MediaType_HTML_UTF8)
//  @Consumes("text/html;charset=utf-8")
  public Response alipayReturn(
    @QueryParam("out_trade_no") String outTradeNo,
    @QueryParam("total_amount") String totalAmount
  ) {
    logger.warning("=================================== alipayReturn:");
    logger.warning("outTradeNo: " + outTradeNo);
//    return SvcUtils.tryOps(
//      () -> {
//        NotifyUtils.parseNotifyResultAndSave2Db(
//
//        );
//      }
//    );
    return SvcUtils.tryOps(
      () -> {
        return Response.ok(
          String.format(NotifyUtils.returnMsgTemplate(), totalAmount)
        ).build();
      },
      SvcAuditUtils.Alipay_Return()
    );

  }
}
