package org.xg;

import org.xg.auth.Secured;
import org.xg.db.api.TDbOps;
import org.xg.db.impl.DbOpsImpl;
import org.xg.db.impl.Utils;
import org.xg.db.model.CommonUtils;
import org.xg.db.model.MCustomer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;

@Path("customer")
public class CustomerOps {
  @Secured
  @GET
  @Path("testAll")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public String allCustomers() {

    try {
      Connection conn = Utils.tryConnect(DbConfig.ConnectionStr);

      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      MCustomer[] customers = dbOps.allCustomers();
      conn.close();
      return MCustomer.toJsons(customers);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }

  }
}
