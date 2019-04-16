package org.xg;

import org.xg.auth.Secured;
import org.xg.dbModels.MCustomerProfile;
import org.xg.dbModels.TDbOps;
import org.xg.dbModels.MCustomer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Logger;

import static org.xg.SvcUtils.*;
@Path("customer")
public class CustomerOps {
  private final static Logger logger = Logger.getLogger(CustomerOps.class.getName());

  @Secured
  @GET
  @Path("testAll")
  @RolesAllowed("user")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public Response allCustomers(@Context SecurityContext sc) {
    String uid = sc.getUserPrincipal().getName();
    return tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();
        MCustomer[] customers = dbOps.allCustomers();
//      conn.close();
        return Response.ok(
          MCustomer.toJsons(customers)
        ).build();
      },
      uid,
      "get all customers",
      "Error running 'allCustomers'"

    );
  }

  @Secured
  @GET
  @Path("profiles")
  @RolesAllowed("user")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public Response getProfiles(@Context SecurityContext sc) {
    String uid = sc.getUserPrincipal().getName();
    return tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();
        MCustomerProfile[] profiles = dbOps.getCustomerProfiles(uid);
//      conn.close();
        return Response.ok(
          MCustomerProfile.toJsons(profiles)
        ).build();
      },
      uid,
      "getProfiles",
      String.format("Error getting profiles for '%s'", uid)
    );
  }

}
