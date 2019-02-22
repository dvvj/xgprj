package org.xg;

import org.xg.db.api.TDbOps;
import org.xg.db.impl.DbOpsImpl;
import org.xg.db.impl.Utils;
import org.xg.db.model.MProduct;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;

@Path("product")
public class ProductOps {
  @GET
  @Path("all")
  @Produces(SvcUtils.MediaType_JSON_UTF8)
  public String allProducts() {

    try {
      Connection conn = Utils.tryConnect(DbConfig.ConnectionStr);

      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      MProduct[] allProducts = dbOps.allProducts();
      conn.close();
      String res = MProduct.toJsons(allProducts);
      return res;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }
  }
}
