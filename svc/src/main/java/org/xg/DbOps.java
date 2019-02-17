package org.xg;

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

  @GET
  @Path("testAllCustomers")
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public String allCustomers() {

    try {
      Connection conn = Utils.tryConnect(
        "jdbc:mysql://10.0.2.15:3306/xgproj?user=dbuser&password=dbpass"
      );

      System.out.println("connected!");

      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      String allCustomers = dbOps.allCustomers(conn);
      conn.close();
      return allCustomers;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }

  }
}
