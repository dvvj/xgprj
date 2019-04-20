package org.xg;

import org.xg.auth.AuthHelpers;
import org.xg.auth.Secured;
import org.xg.busiLogic.PricePlanLogics;
import org.xg.busiLogic.RewardPlanLogics;
import org.xg.dbModels.*;
import org.xg.svc.UpdatePassword;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

@Path("user")
public class UserCfgOps {
  private final static Logger logger = Logger.getLogger(UserCfgOps.class.getName());

  @Secured
  @GET
  @Path("pricePlan")
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getPricePlan(@Context SecurityContext sc) {
    String uid = sc.getUserPrincipal().getName();
    return SvcUtils.tryOps(
      () -> {
        MCustomer customer = SvcUtils.getCustomers().get(uid);
        String plansJson = PricePlanLogics.pricePlanJsonForJ(
          customer,
          PricePlanUtils.getPricePlanMaps(),
          PricePlanUtils.getPricePlans()
        );

//      if (pricePlan == null)
//        logger.info(String.format("No price plan found for user [%s]", uid));

        return Response.ok(plansJson)
          .build();
      },
      uid,
      "getUserPricePlan",
      String.format("Error getting price plan for [%s]", uid)
    );
  }

  @Secured
  @GET
  @Path("allPricePlans")
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getAllPricePlans(@Context SecurityContext sc) {
    String uid = sc.getUserPrincipal().getName();
    return SvcUtils.tryOps(
      () -> {
        Map<String, MPricePlan> planMap = PricePlanUtils.getPricePlans();
        MPricePlan[] allPlans = new MPricePlan[planMap.size()];
        ArrayList<MPricePlan> t = new ArrayList<>(planMap.size());
        t.addAll(planMap.values());
        allPlans = t.toArray(allPlans);
        String json = MPricePlan.toJsons(allPlans);

        return Response.ok(json)
          .build();
      },
      uid,
      "getUserPricePlan",
      String.format("Error getting price plan for [%s]", uid)
    );
  }

  @Secured
  @POST
  @Path("pricePlanAccessibleBy")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getPricePlanAccessibleBy(String creatorId) {
    try {
      MPricePlan[] pricePlans = PricePlanUtils.getPricePlansAccessibleBy(creatorId);

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
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response addPricePlan(String json) {
    try {
      MPricePlan plan = MPricePlan.fromJson(json);
      TDbOps dbOps = SvcUtils.getDbOps();

      String planId = dbOps.addPricePlan(plan);

      PricePlanUtils.updatePricePlans();

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
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getRewardPlan(String uid) {
    try {
      //MMedProf prof = SvcUtils.getMedProfs().get(uid);
      String plansJson = RewardPlanLogics.rewardPlanJsonForJ(
        uid,
        RewardPlanUtils.getRewardPlanMaps(),
        RewardPlanUtils.getRewardPlans()
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
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response addRewardPlan(String json) {
    try {
      MRewardPlan plan = MRewardPlan.fromJson(json);
      TDbOps dbOps = SvcUtils.getDbOps();

      String planId = dbOps.addRewardPlan(plan);

      RewardPlanUtils.updateRewardPlans();

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
  @Path("rewardPlanAccessibleBy")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response rewardPlanAccessibleBy(String creatorId) {
    try {
      MRewardPlan[] rewardPlans = RewardPlanUtils.getRewardPlansAccessibleBy(creatorId);

      String j = MRewardPlan.toJsons(rewardPlans);

      return Response.ok(j).build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }

  @Secured
  @POST
  @Path("updateCustomerPassword")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response updatePassword(String updatePasswordJson, @Context SecurityContext sc) {
    String uid = sc.getUserPrincipal().getName();
    return SvcUtils.tryOps(
      () -> {
        UpdatePassword up = UpdatePassword.fromJson(updatePasswordJson);

        byte[] oldPassHash = AuthHelpers.str2Hash(up.oldpassHash());
        byte[] newPassHash = AuthHelpers.str2Hash(up.newpassHash());

        OpResp dbResp = SvcUtils.getDbOps().updateCustomerPass(
          uid, oldPassHash, newPassHash
        );

        if (dbResp.success()) {
          SvcUtils.updateCustomers();
          return Response.ok("password updated for " + uid)
            .build();
        }
        else {
          return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(dbResp.errMsgJ())
            .build();
        }
      },
      uid,
      "updatePassword",
      String.format(
        "updatePassword error, updatePasswordJson: %s", updatePasswordJson
      )
    );
  }
}
