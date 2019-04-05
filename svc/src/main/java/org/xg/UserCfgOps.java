package org.xg;

import org.xg.auth.Secured;
import org.xg.busiLogic.PricePlanLogics;
import org.xg.busiLogic.RewardPlanLogics;
import org.xg.dbModels.*;

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

  @Secured
  @POST
  @Path("pricePlanCreatedBy")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response getPricePlanCreatedBy(String creatorId) {
    try {
      MPricePlan[] pricePlans = SvcUtils.getPricePlansCreatedBy(creatorId);

      String j = MPricePlan.toJsons(pricePlans);
//      if (pricePlan == null)
//        logger.info(String.format("No price plan found for user [%s]", uid));

      return Response.ok(j).build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }

  @Secured
  @POST
  @Path("addPricePlan")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response addPricePlan(String json) {
    try {
      MPricePlan plan = MPricePlan.fromJson(json);
      TDbOps dbOps = SvcUtils.getDbOps();

      String planId = dbOps.addPricePlan(plan);

      SvcUtils.updatePricePlans();

      return Response.status(Response.Status.CREATED)
        .entity(planId)
        .build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }

  @Secured
  @POST
  @Path("rewardPlan")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response getRewardPlan(String uid) {
    try {
      //MMedProf prof = SvcUtils.getMedProfs().get(uid);
      String plansJson = RewardPlanLogics.rewardPlanJsonForJ(
        uid,
        SvcUtils.getRewardPlanMaps(),
        SvcUtils.getRewardPlans()
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

  @Secured
  @POST
  @Path("addRewardPlan")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response addRewardPlan(String json) {
    try {
      MRewardPlan plan = MRewardPlan.fromJson(json);
      TDbOps dbOps = SvcUtils.getDbOps();

      String planId = dbOps.addRewardPlan(plan);

      SvcUtils.invalidatedRewardPlans();

      return Response.status(Response.Status.CREATED)
        .entity(planId)
        .build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }


  @Secured
  @POST
  @Path("rewardPlanCreatedBy")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response rewardPlanCreatedBy(String creatorId) {
    try {
      MRewardPlan[] rewardPlans = SvcUtils.getRewardPlansCreatedBy(creatorId);

      String j = MRewardPlan.toJsons(rewardPlans);
//      if (pricePlan == null)
//        logger.info(String.format("No price plan found for user [%s]", uid));

      return Response.ok(j).build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }

}
