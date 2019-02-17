package org.xg;

import org.xg.auth.UserPassPost;

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
    UserPassPost upp = UserPassPost.fromJson(userPassPostJson);
    return Response.status(200)
      .entity(upp.toString())
      .build();
  }
}
