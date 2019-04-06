package org.xg;

import org.xg.auth.UserDbAuthority;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

  @POST
  @Path("alipayReturn")
  @Consumes("text/html;charset=utf-8")
//  @Consumes("text/html;charset=utf-8")
  public Response alipayReturn(String returnContent) {
    logger.warning("=================================== alipayReturn:");
    logger.warning(returnContent);
    return Response.ok().build();
  }
}
