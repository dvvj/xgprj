package org.xg;

import org.xg.db.api.TDbOps;
import org.xg.db.impl.DbOpsImpl;
import org.xg.db.impl.Utils;
import org.xg.db.model.MOrder;
import org.xg.svc.UserOrder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.logging.Logger;

@Path("order")
public class OrderOps {

  private final static Logger logger = Logger.getLogger(OrderOps.class.getName());


  @POST
  @Path("testCurrUser")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public Response userOrders(String userId) {
    try {
      Connection conn = Utils.tryConnect(DbConfig.ConnectionStr);

      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      MOrder[] orders = dbOps.ordersOf(userId);
      conn.close();
      return Response.ok(
        MOrder.toJsons(orders)
      ).build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }
  }

  @PUT
  @Path("placeOrder")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response placeOrder(String orderJson) {
    UserOrder userOrder = UserOrder.fromJson(orderJson);
    try {
      Connection conn = Utils.tryConnect(DbConfig.ConnectionStr);

      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      Long orderId = dbOps.placeOrder(
        userOrder.uid(), userOrder.productId(), userOrder.qty()
      );
      conn.close();

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
