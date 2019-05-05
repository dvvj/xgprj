package org.xg;

import org.xg.audit.SvcAuditUtils;
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
    return SvcUtils.tryOps(
      () -> {
        MProfOrgAgent[] agents = SvcUtils.getProfOrgAgentsOf(sc.getUserPrincipal().getName());
        String j = MProfOrgAgent.toJsons(agents);

        return Response.ok(j)
          .build();
      },
      sc,
      SvcAuditUtils.ProfOrg_GetAgents()
    );
  }


  @Secured
  @GET
  @RolesAllowed("user")
  @Path("agentRewardPlans")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getAgentRewardPlansOf(@Context SecurityContext sc) {

    return SvcUtils.tryOps(
      () -> {
        MRewardPlanMap[] rewardPlanMaps = SvcUtils.getDbOps().allRewardPlanMaps();
        String j = MRewardPlanMap.toJsons(rewardPlanMaps);

        return Response.ok(j)
          .build();
      },
      sc,
      SvcAuditUtils.ProfOrg_AgentRewardPlans()
    );
  }

  @Secured
  @GET
  @RolesAllowed("user")
  @Path("rewardPlans")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getRewardPlansOf(@Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();

        MRewardPlan[] plans = dbOps.allRewardPlans(); // todo, optimize
        String j = MRewardPlan.toJsons(plans);

        return Response.ok(j)
          .build();
      },
      sc,
      SvcAuditUtils.ProfOrg_RewardPlans()
    );

  }


  @Secured
  @GET
  @RolesAllowed("user")
  @Path("orderStats4Org")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getOrderStats(@Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();

        MOrgOrderStat[] orderStats = dbOps.orderStatsOfOrg(sc.getUserPrincipal().getName());
        String j = MOrgOrderStat.toJsons(orderStats);

        return Response.ok(j)
          .build();
      },
      sc,
      SvcAuditUtils.ProfOrg_OrderStats4Org()
    );
  }

}
