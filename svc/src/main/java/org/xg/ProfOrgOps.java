package org.xg;

import org.xg.auth.Secured;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MProfOrgAgent;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("profOrg")
public class ProfOrgOps {
  @Secured
  @POST
  @Path("agents")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response getProfsOf(String orgId) {
    try {
      MProfOrgAgent[] agents = SvcUtils.getProfOrgAgentsOf(orgId);
      String j = MProfOrgAgent.toJsons(agents);

      return Response.ok(j)
        .build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }

}
