package org.xg;

import org.xg.auth.Secured;
import org.xg.db.api.TDbOps;
import org.xg.db.impl.DbOpsImpl;
import org.xg.db.impl.Utils;
import org.xg.db.model.MCustomer;
import org.xg.hbn.HbnDbOpsImpl;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.util.logging.Logger;

@Path("customer")
public class CustomerOps {
  private final static Logger logger = Logger.getLogger(CustomerOps.class.getName());

  @Secured
  @GET
  @Path("testAll")
  @RolesAllowed("user")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public Response allCustomers(@Context SecurityContext sc) {
    logger.warning("(To be removed) " + sc.getUserPrincipal());
    try {
//      Connection conn = Utils.tryConnect(SvcUtils.getCfg().infoDbConnStr());
//      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      TDbOps dbOps = SvcUtils.getDbOps();
      MCustomer[] customers = dbOps.allCustomers();
//      conn.close();
      return Response.ok(
        MCustomer.toJsons(customers)
      ).build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }

  }
}
