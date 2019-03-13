package org.xg;

import org.xg.auth.Secured;
import org.xg.busiLogic.PricePlanLogics;
import org.xg.dbModels.MCustomer;
import org.xg.dbModels.TDbOps;
import org.xg.pay.pricePlan.TPricePlan;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("user")
public class UserCfgOps {
  private final static Logger logger = Logger.getLogger(UserCfgOps.class.getName());

  @Secured
  @POST
  @Path("pricePlan")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response getPricePlan(String uid) {
    try {
      MCustomer customer = SvcUtils.getCustomers().get(uid);
      String plansJson = PricePlanLogics.pricePlanJsonForJ(
        customer,
        SvcUtils.getPricePlanMaps(),
        SvcUtils.getPricePlans()
      );

//      if (pricePlan == null)
//        logger.info(String.format("No price plan found for user [%s]", uid));

      return Response.ok(plansJson)
        .build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }
}
