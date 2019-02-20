package org.xg;

import org.xg.auth.Secured;
import org.xg.db.api.TDbOps;
import org.xg.db.impl.DbOpsImpl;
import org.xg.db.impl.Utils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;

@Path("db")
public class DbOps {

  @Secured
  @GET
  @Path("testAllCustomers")
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public String allCustomers() {

    try {
      Connection conn = Utils.tryConnect(DbConfig.ConnectionStr);

      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      String allCustomers = dbOps.allCustomers();
      conn.close();
      return allCustomers;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }

  }

  @GET
  @Path("allProducts")
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public String allProducts() {

    try {
      Connection conn = Utils.tryConnect(DbConfig.ConnectionStr);

      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      String allProducts = dbOps.allProducts();
      conn.close();
      return allProducts;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }

  }
}
