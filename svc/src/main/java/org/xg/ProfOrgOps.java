package org.xg;

import org.xg.auth.Secured;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.dbModels.MProfOrgAgent;
import org.xg.dbModels.TDbOps;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("profOrg")
public class ProfOrgOps {
  private final static Logger logger = Logger.getLogger(ProfOrgOps.class.getName());


  @Secured
  @POST
  @Path("agents")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getProfsOf(String orgId) {
    try {
      MProfOrgAgent[] agents = SvcUtils.getProfOrgAgentsOf(orgId);
      String j = MProfOrgAgent.toJsons(agents);

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
  @POST
  @Path("orderStats4Org")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getOrderStats(String orgId) {
    try {
      TDbOps dbOps = SvcUtils.getDbOps();

      MOrgOrderStat[] orderStats = dbOps.orderStatsOfOrg(orgId);
      String j = MOrgOrderStat.toJsons(orderStats);

      return Response.ok(j)
        .build();
    }
    catch (Exception ex) {
      logger.warning("Error getting orderStats: " + ex.getMessage());
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }

}
