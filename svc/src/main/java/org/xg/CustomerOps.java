package org.xg;

import org.xg.audit.SvcAuditUtils;
import org.xg.auth.Secured;
import org.xg.dbModels.MCustomerProfile;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.TDbOps;
import org.xg.dbModels.MCustomer;
import org.xg.svc.UserOrder;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.logging.Logger;

import static org.xg.SvcUtils.*;
@Path("customer")
public class CustomerOps {
  private final static Logger logger = Logger.getLogger(CustomerOps.class.getName());

//  @Secured
//  @GET
//  @Path("testAll")
//  @RolesAllowed("user")
//  @Produces(SvcUtils.MediaType_JSON_UTF8)
//  public Response allCustomers(@Context SecurityContext sc) {
//    return tryOps(
//      () -> {
//        TDbOps dbOps = SvcUtils.getDbOps();
//        MCustomer[] customers = dbOps.allCustomers();
//        return Response.ok(
//          MCustomer.toJsons(customers)
//        ).build();
//      },
//      sc,
//      SvcAuditUtils.Customer_TestAll()
//    );
//  }

  @Secured
  @GET
  @Path("profiles")
  @RolesAllowed("user")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public Response getProfiles(@Context SecurityContext sc) {
    return tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();
        MCustomerProfile[] profiles = dbOps.getCustomerProfiles(sc.getUserPrincipal().getName());
//      conn.close();
        return Response.ok(
          MCustomerProfile.toJsons(profiles)
        ).build();
      },
      sc,
      SvcAuditUtils.Customer_GetProfiles()
    );
  }

  @Secured
  @GET
  @Path("referringMedProfs")
  @RolesAllowed("user")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public Response referringMedProfs(@Context SecurityContext sc) {
    return tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();
        MCustomerProfile[] profiles = dbOps.getCustomerProfiles(sc.getUserPrincipal().getName());

        String[] profIds = Arrays.stream(profiles).map(MCustomerProfile::profId).toArray(String[]::new);
        MMedProf[] res = dbOps.medprofsByIds(profIds);

        return Response.ok(
          MMedProf.toJsons(res)
        ).build();
      },
      sc,
      SvcAuditUtils.Customer_ReferringMedProfs()
    );
  }


  @Secured
  @PUT
  @Path("placeOrder")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response placeOrder(String orderJson, @Context SecurityContext sc) {
    UserOrder userOrder = UserOrder.fromJson(orderJson);
    return tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();
        //String uid = sc.getUserPrincipal().getName(); //userOrder.uid();
        MCustomer customer = SvcUtils.getCustomers().get(userOrder.uid());
        MMedProf prof = SvcUtils.getMedProfs().get(customer.refUid());
        String profOrgAgentId = SvcUtils.getProfOrgAgent(prof.profId()).orgAgentId();

        Long orderId = dbOps.placeOrder(
          userOrder.uid(), customer.refUid(),
          profOrgAgentId, userOrder.productId(), userOrder.qty(), userOrder.actualCost()
        );

        String msg = String.format("Created Order (id: %d)", orderId);
        logger.info(msg);

        return Response.status(Response.Status.CREATED)
          .entity(orderId)
          .build();
      },
      sc,
      SvcAuditUtils.Customer_PlaceOrder()
    );
  }
}
