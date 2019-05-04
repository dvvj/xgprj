package org.xg;

import org.xg.audit.SvcAuditUtils;
import org.xg.auth.Secured;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MOrgAgentOrderStat;
import org.xg.dbModels.MRewardPlan;
import org.xg.dbModels.MRewardPlanMap;
import org.xg.svc.AddNewMedProf;
import org.xg.svc.CustomerPricePlan;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("profOrgAgent")
public class ProfOrgAgentOps {
  @Secured
  @GET
  @Path("profs")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getProfsOf(@Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        MMedProf[] medprofs = SvcUtils.getMedProfsOf(sc.getUserPrincipal().getName());
        String j = MMedProf.toJsons(medprofs);

        return Response.ok(j)
          .build();
      },
      sc,
      SvcAuditUtils.ProfOrgAgent_GetProfs()
    );
  }

  @Secured
  @GET
  @Path("orderStats")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getOrderStatsOf(@Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        MOrgAgentOrderStat[] orderStats = SvcUtils.getOrgAgentOrderStatsOf(sc.getUserPrincipal().getName());
        String j = MOrgAgentOrderStat.toJsons(orderStats);

        return Response.ok(j)
          .build();
      },
      sc,
      SvcAuditUtils.ProfOrgAgent_GetOrderStatsOf()
    );

  }

  @Secured
  @POST
  @Path("addNewMedProf")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response addNewMedProf(String addNewMedProfJson, @Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        AddNewMedProf mp = AddNewMedProf.fromJson(addNewMedProfJson);

        SvcUtils.addNewMedProf(mp);

        MRewardPlanMap rpm = mp.rewardPlan();
        RewardPlanUtils.addRewardPlanMap(rpm);

        return Response.status(Response.Status.CREATED)
          .build();
      },
      sc,
      SvcAuditUtils.ProfOrgAgent_AddNewMedProf()
    );
  }

}
