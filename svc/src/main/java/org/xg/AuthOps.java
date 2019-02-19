package org.xg;

import org.xg.auth.AuthHelpers;
import org.xg.auth.AuthResp;
import org.xg.auth.CustomerDbAuthority;
import org.xg.svc.UserPass;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.ZonedDateTime;


@Path("auth")
public class AuthOps {

  @POST
  @Path("userPass")
  @Consumes(MediaType.TEXT_PLAIN)
  public Response authorize(String userPassPostJson) {
    UserPass up = UserPass.fromJson(userPassPostJson);

    boolean authenticated = CustomerDbAuthority.authenticate(up.uid(), up.passHashStr());

    if (authenticated) {
      AuthResp resp = AuthResp.authSuccess(up);
      return Response.ok(AuthResp.toJson(resp)).build();
    }
    else {
      return Response.status(Response.Status.UNAUTHORIZED)
        .entity(String.format("user [%s] NOT authorized!", up.uid()))
        .build();
    }
  }
}
