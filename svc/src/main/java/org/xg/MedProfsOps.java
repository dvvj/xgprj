package org.xg;

import org.xg.auth.Secured;
import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MMedProf;
import org.xg.svc.AddNewCustomer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("prof")
public class MedProfsOps {
  @Secured
  @POST
  @Path("customers")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response getCustomers(String profId) {
    try {
      //MMedProf prof = SvcUtils.getMedProfs().get(profId);
      MCustomer[] customers = SvcUtils.getCustomersRefedBy(profId);
      String j = MCustomer.toJsons(customers);

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
  @Path("newCustomer")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
  public Response newCustomer(String newCustomerJson) {
    try {
      //MMedProf prof = SvcUtils.getMedProfs().get(profId);
      AddNewCustomer addNewCustomer = AddNewCustomer.fromJson(newCustomerJson);

      SvcUtils.addNewCustomer(addNewCustomer);

      return Response.status(Response.Status.CREATED)
        .build();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new WebApplicationException("Error", ex);
    }
  }


}
