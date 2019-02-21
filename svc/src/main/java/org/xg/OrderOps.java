package org.xg;

import org.xg.db.api.TDbOps;
import org.xg.db.impl.DbOpsImpl;
import org.xg.db.impl.Utils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;

@Path("order")
public class OrderOps {

  @POST
  @Path("testCurrUser")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response userOrders(String userId) {
    try {
      Connection conn = Utils.tryConnect(DbConfig.ConnectionStr);

      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      String orders = dbOps.ordersOf(userId);
      conn.close();
      return Response.ok(orders).build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }

  }
}
