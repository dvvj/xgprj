package org.xg;

import org.xg.auth.Secured;
import org.xg.db.api.TDbOps;
import org.xg.db.impl.DbOpsImpl;
import org.xg.db.impl.Utils;
import org.xg.db.model.MOrder;
import org.xg.hbn.HbnDbOpsImpl;
import org.xg.svc.UserOrder;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.util.logging.Logger;

@Path("order")
public class OrderOps {

  private final static Logger logger = Logger.getLogger(OrderOps.class.getName());

  @Secured
  @GET
  @Path("testCurrUser")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public Response userOrders(@Context SecurityContext sc) {
    try {
      String uid = sc.getUserPrincipal().getName();
      logger.warning(
        String.format("Getting orders for user [%s]", uid)
      );
//      Connection conn = Utils.tryConnect(SvcUtils.getCfg().infoDbConnStr());
//      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      TDbOps dbOps = HbnDbOpsImpl.hbnOps();
      MOrder[] orders = dbOps.ordersOf(uid);
//      conn.close();
      return Response.ok(
        MOrder.toJsons(orders)
      ).build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }
  }

  @Secured
  @PUT
  @Path("placeOrder")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response placeOrder(String orderJson, @Context SecurityContext sc) {
    UserOrder userOrder = UserOrder.fromJson(orderJson);
    try {
//      Connection conn = Utils.tryConnect(SvcUtils.getCfg().infoDbConnStr());
//      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      TDbOps dbOps = HbnDbOpsImpl.hbnOps();
      String uid = sc.getUserPrincipal().getName(); //userOrder.uid();

      Long orderId = dbOps.placeOrder(
        userOrder.uid(), userOrder.productId(), userOrder.qty()
      );
//      conn.close();

      String msg = String.format("Created Order (id: %d)", orderId);
      logger.info(msg);

      return Response.status(Response.Status.CREATED)
        .entity(msg)
        .build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }

  }
}
