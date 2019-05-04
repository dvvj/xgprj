package org.xg;

import org.xg.audit.SvcAuditUtils;
import org.xg.auth.Secured;
import org.xg.auth.SvcHelpers;
import org.xg.dbModels.*;
import org.xg.svc.PayOrder;
import org.xg.svc.UserOrder;
import org.xg.user.UserType;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Logger;

@Path("order")
public class OrderOps {

  private final static Logger logger = Logger.getLogger(OrderOps.class.getName());

  @Secured
  @GET
  @Path("currUser")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public Response userOrders(@Context SecurityContext sc) {
    try {
      String uid = sc.getUserPrincipal().getName();
      logger.warning(
        String.format("Getting orders for user [%s]", uid)
      );

      TDbOps dbOps = SvcUtils.getDbOps();
      MOrder[] orders = dbOps.ordersOf(uid);
      return Response.ok(
        MOrder.toJsons(orders)
      ).build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }


  @Secured
  @PUT
  @Path("placeOrder")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response placeOrder(String orderJson, @Context SecurityContext sc) {
    UserOrder userOrder = UserOrder.fromJson(orderJson);
    try {
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
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }

//  @Secured
//  @POST
//  @Path("payOrder")
//  @Consumes(MediaType.TEXT_PLAIN)
//  @Produces(SvcUtils.MediaType_TXT_UTF8)
//  public Response payOrder(String payOrderJson, @Context SecurityContext sc) {
//    return SvcUtils.tryOps(
//      () -> {
//        PayOrder payOrder = PayOrder.fromJson(payOrderJson);
//        TDbOps dbOps = SvcUtils.getDbOps();
//        dbOps.payOrder(payOrder.orderId());
//
//        String msg = String.format("Order (id: %d) payed", payOrder.orderId());
//        logger.info(msg);
//
//        return Response.ok(msg)
//          .build();
//      },
//      String.format(
//        "payOrder error, payOrderJson: %s", payOrderJson
//      )
//    );
//  }

  @Secured
  @POST
  @Path("cancelOrder")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response cancelOrder(String orderId, @Context SecurityContext sc) {
    String uid = sc.getUserPrincipal().getName();
    return SvcUtils.tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();
        dbOps.cancelOrder(Long.parseLong(orderId));

        String msg = String.format("Order (id: %s for user: %s) cancelled", orderId, uid);
        logger.info(msg);

        return Response.ok(msg)
          .build();
      },
      uid,
      "cancelOrder",
      String.format(
        "cancelOrder error, orderId: %s", orderId
      )
    );
  }

}
