package org.xg;

import org.xg.auth.CustomerDbAuthority;
import org.xg.auth.UserPass;
import org.xg.auth.UserPassBase64;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
public class AuthOps {

  @POST
  @Path("userPass")
  @Consumes(MediaType.TEXT_PLAIN)
  public Response authorize(String userPassPostJson) {
    UserPass up = UserPassBase64.decFromJson(userPassPostJson);

    boolean authenticated = CustomerDbAuthority.authenticate(up.uid(), up.passHash());
    Response resp = authenticated ?
      Response.ok(
        String.format("user [%s] authorized!", up.uid())
      ).build()
      : Response.status(Response.Status.UNAUTHORIZED)
      .entity(String.format("user [%s] NOT authorized!", up.uid()))
      .build();
    return resp;
  }
}
