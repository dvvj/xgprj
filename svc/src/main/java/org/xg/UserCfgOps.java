package org.xg;

import org.xg.audit.SvcAuditUtils;
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
    return SvcUtils.tryOps(
      () -> {
        MCustomer customer = SvcUtils.getCustomers().get(sc.getUserPrincipal().getName());
        String plansJson = PricePlanLogics.pricePlanJsonForJ(
          customer,
          PricePlanUtils.getPricePlanMaps(),
          PricePlanUtils.getPricePlans()
        );

        return Response.ok(plansJson)
          .build();
      },
      sc,
      SvcAuditUtils.UserCfg_GetPricePlan()
    );
  }

  @Secured
  @GET
  @Path("allPricePlans")
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getAllPricePlans(@Context SecurityContext sc) {
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
      sc,
      SvcAuditUtils.UserCfg_GetAllPricePlan()
    );
  }

  @Secured
  @GET
  @Path("pricePlanAccessibleBy")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getPricePlanAccessibleBy(@Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        MPricePlan[] pricePlans = PricePlanUtils.getPricePlansAccessibleBy(sc.getUserPrincipal().getName());

        String j = MPricePlan.toJsons(pricePlans);

        return Response.ok(j).build();
      },
      sc,
      SvcAuditUtils.UserCfg_GetAllPricePlan()
    );
  }

  @Secured
  @GET
  @Path("allProducts")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public Response allProducts(@Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();
        MProduct[] allProducts = dbOps.allProducts();
        String res = MProduct.toJsons(allProducts);
        return Response.ok(res).build();
      },
      sc,
      SvcAuditUtils.UserCfg_AllProducts()
    );
  }

  @Secured
  @POST
  @Path("addPricePlan")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response addPricePlan(String json, @Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        MPricePlan plan = MPricePlan.fromJson(json);
        TDbOps dbOps = SvcUtils.getDbOps();
        String planId = dbOps.addPricePlan(plan);
        PricePlanUtils.updatePricePlans();
        return Response.status(Response.Status.CREATED)
          .entity(planId)
          .build();
      },
      sc,
      SvcAuditUtils.UserCfg_AddPricePlan()
    );

  }

  @Secured
  @GET
  @Path("rewardPlan")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getRewardPlan(@Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        String plansJson = RewardPlanLogics.rewardPlanJsonForJ(
          sc.getUserPrincipal().getName(),
          RewardPlanUtils.getRewardPlanMaps(),
          RewardPlanUtils.getRewardPlans()
        );

        return Response.ok(plansJson)
          .build();
      },
      sc,
      SvcAuditUtils.UserCfg_AddPricePlan()
    );

  }

  @Secured
  @POST
  @Path("addRewardPlan")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response addRewardPlan(String json, @Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        MRewardPlan plan = MRewardPlan.fromJson(json);
        TDbOps dbOps = SvcUtils.getDbOps();

        String planId = dbOps.addRewardPlan(plan);

        RewardPlanUtils.updateRewardPlans();

        return Response.status(Response.Status.CREATED)
          .entity(planId)
          .build();
      },
      sc,
      SvcAuditUtils.UserCfg_AddRewardPlan()
    );

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
