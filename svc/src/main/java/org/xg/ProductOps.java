package org.xg;

import org.xg.db.api.TDbOps;
import org.xg.dbModels.MProduct;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("product")
public class ProductOps {
  @GET
  @Path("all")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public Response allProducts() {

    try {
//      Connection conn = Utils.tryConnect(SvcUtils.getCfg().infoDbConnStr());
//      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      TDbOps dbOps = SvcUtils.getDbOps();
      MProduct[] allProducts = dbOps.allProducts();
//      conn.close();
      String res = MProduct.toJsons(allProducts);
      return Response.ok(res).build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }
  }
}
