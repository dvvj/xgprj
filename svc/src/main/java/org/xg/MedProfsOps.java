package org.xg;

import org.xg.audit.SvcAuditUtils;
import org.xg.auth.Secured;
import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MCustomerProfile;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.TDbOps;
import org.xg.json.CommonUtils;
import org.xg.svc.AddNewCustomer;
import org.xg.svc.CustomerPricePlan;
import org.xg.user.UserType;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Logger;

@Path("prof")
public class MedProfsOps {

  private final static Logger logger = Logger.getLogger(MedProfsOps.class.getName());

  @Secured
  @GET
  @Path("customers")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getCustomers(@Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        MCustomer[] customers = SvcUtils.getCustomersRefedBy(sc.getUserPrincipal().getName());
        String j = MCustomer.toJsons(customers);

        return Response.ok(j)
          .build();
      },
      sc,
      SvcAuditUtils.MedProf_GetCustomers()
    );
  }

  @Secured
  @POST
  @Path("findCustomerById")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response findCustomerById(String customerId, @Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        String cid = UserType.Customer().genUid(customerId);
        MCustomer customer = SvcUtils.getCustomerById(cid);
        String j = (customer != null) ? MCustomer.toJson(customer) : "";

        return Response.ok(j)
          .build();
      },
      sc,
      SvcAuditUtils.MedProf_FindCustomerById()
    );
  }

  @Secured
  @POST
  @Path("newCustomer")
  @Consumes(SvcUtils.MediaType_TXT_UTF8)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response newCustomer(String newCustomerJson, @Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        AddNewCustomer addNewCustomer = AddNewCustomer.fromJson(newCustomerJson);

        SvcUtils.addNewCustomer(addNewCustomer);
        logger.info(
          String.format("New customer added: id [%s], name [%s]",
            addNewCustomer.customer().uid(),
            addNewCustomer.customer().name()
          )
        );

        if (addNewCustomer.ppm() != null) {
          PricePlanUtils.addPricePlanMap(addNewCustomer.ppm());
          logger.info(
            String.format("Price plan mapped: ids [%s]",
              addNewCustomer.ppm().planIdStr()
            )
          );
        }

        return Response.status(Response.Status.CREATED)
          .build();
      },
      sc,
      SvcAuditUtils.MedProf_NewCustomer()
    );
  }

  @Secured
  @POST
  @Path("newProfileExistingCustomer")
  @Consumes(SvcUtils.MediaType_TXT_UTF8)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response newProfileExistingCustomer(String newProfileExistingCustomerJson, @Context SecurityContext sc) {
//    String profId = sc.getUserPrincipal().getName();

    return SvcUtils.tryOps(
      () -> {
        MCustomerProfile cp = MCustomerProfile.fromJson(newProfileExistingCustomerJson);
        long newProfileId = SvcUtils.getDbOps().createCustomerProfile(
          cp.profId(), cp.customerId(), cp.detailedInfo(), cp.version()
        );
        return Response.ok(newProfileId).build();
      },
      sc,
      SvcAuditUtils.MedProf_NewProfileExistingCustomer()
    );
  }


  @Secured
  @POST
  @Path("existingCustomerProfiles")
  @Consumes(SvcUtils.MediaType_TXT_UTF8)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response existingCustomerProfiles(String customerId, @Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        TDbOps dbOps = SvcUtils.getDbOps();
        String cid = UserType.Customer().genUid(customerId);
        MCustomerProfile[] profiles = dbOps.getCustomerProfiles(cid);

        return Response.ok(
          MCustomerProfile.toJsons(profiles)
        ).build();
      },
      sc,
      SvcAuditUtils.MedProf_ExistingCustomerProfiles()
    );
  }

  @Secured
  @GET
  @Path("customerPricePlans")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(SvcUtils.MediaType_TXT_UTF8)
  public Response getCustomerPricePlans(@Context SecurityContext sc) {
    return SvcUtils.tryOps(
      () -> {
        CustomerPricePlan[] res = PricePlanUtils.getPricePlanMap4Agent(sc.getUserPrincipal().getName());
        String j = CustomerPricePlan.toJsons(res);

        return Response.ok(j).build();
      },
      sc,
      SvcAuditUtils.MedProf_GetCustomerPricePlans()
    );
  }
}
