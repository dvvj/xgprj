package org.xg;

import org.xg.auth.Secured;
import org.xg.dbModels.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Logger;

@Path("profOrg")
public class ProfOrgOps {
  private final static Logger logger = Logger.getLogger(ProfOrgOps.class.getName());


  @Secured
  @GET
  @RolesAllowed("user")
  @Path("agents")
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getAgentsOf(@Context SecurityContext sc) {
    String orgId = sc.getUserPrincipal().getName();
    return SvcUtils.tryOps(
      () -> {
        MProfOrgAgent[] agents = SvcUtils.getProfOrgAgentsOf(orgId);
        String j = MProfOrgAgent.toJsons(agents);

        return Response.ok(j)
          .build();
      },
      orgId,
      "getAgentsOf",
      String.format("Error getting agents of [%s]", orgId)
    );
  }


  @Secured
  @GET
  @RolesAllowed("user")
  @Path("agentRewardPlans")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getAgentRewardPlansOf(@Context SecurityContext sc) {
    String orgId = sc.getUserPrincipal().getName();
    try {
//      MProfOrgAgent[] agents = SvcUtils.getProfOrgAgentsOf(orgId);
//      String j = MProfOrgAgent.toJsons(agents);
      MRewardPlanMap[] rewardPlanMaps = SvcUtils.getDbOps().allRewardPlanMaps();
      String j = MRewardPlanMap.toJsons(rewardPlanMaps);

      return Response.ok(j)
        .build();
    }
    catch (Exception ex) {
      logger.warning("Error getting agents: " + ex.getMessage());
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }

  @Secured
  @GET
  @RolesAllowed("user")
  @Path("rewardPlans")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getRewardPlansOf(@Context SecurityContext sc) {
    String orgId = sc.getUserPrincipal().getName();
    return SvcUtils.tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();

        MRewardPlan[] plans = dbOps.allRewardPlans(); // todo, optimize
        String j = MRewardPlan.toJsons(plans);

        return Response.ok(j)
          .build();
      },
      orgId,
      "rewardPlans",
      String.format("Error getting reward plans for [%s]", orgId)
    );
  }


  @Secured
  @GET
  @RolesAllowed("user")
  @Path("orderStats4Org")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getOrderStats(@Context SecurityContext sc) {
    String orgId = sc.getUserPrincipal().getName();
    return SvcUtils.tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();

        MOrgOrderStat[] orderStats = dbOps.orderStatsOfOrg(orgId);
        String j = MOrgOrderStat.toJsons(orderStats);

        return Response.ok(j)
          .build();
      },
      orgId,
      "orderStats4Org",
      String.format(
        "Error getting orderStats for org [%s]", orgId
      )
    );
//    try {
//      TDbOps dbOps = SvcUtils.getDbOps();
//
//      MOrgOrderStat[] orderStats = dbOps.orderStatsOfOrg(orgId);
//      String j = MOrgOrderStat.toJsons(orderStats);
//
//      return Response.ok(j)
//        .build();
//    }
//    catch (Exception ex) {
//      logger.warning("Error getting orderStats: " + ex.getMessage());
//      ex.printStackTrace();
//      throw new WebApplicationException("Error", ex);
//    }
  }

}
