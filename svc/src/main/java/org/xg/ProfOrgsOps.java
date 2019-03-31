package org.xg;

import org.xg.auth.Secured;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MOrgAgentOrderStat;
import org.xg.svc.AddNewMedProf;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("profOrg")
public class ProfOrgsOps {
  @Secured
  @POST
  @Path("profs")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response getProfsOf(String orgId) {
    try {
      MMedProf[] medprofs = SvcUtils.getMedProfsOf(orgId);
      String j = MMedProf.toJsons(medprofs);

      return Response.ok(j)
        .build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }

  @Secured
  @POST
  @Path("orderStats")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response getOrderStatsOf(String orgId) {
    try {
      MOrgAgentOrderStat[] orderStats = SvcUtils.getOrgOrderStatsOf(orgId);
      String j = MOrgAgentOrderStat.toJsons(orderStats);

      return Response.ok(j)
        .build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }

  @Secured
  @POST
  @Path("addNewMedProf")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response addNewMedProf(String addNewMedProfJson) {
    try {
      AddNewMedProf mp = AddNewMedProf.fromJson(addNewMedProfJson);

      SvcUtils.addNewMedProf(mp);

      return Response.status(Response.Status.CREATED)
        .build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }
}
