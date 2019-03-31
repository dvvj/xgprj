package org.xg;

import org.xg.auth.AuthResp;
import org.xg.auth.UserDbAuthority;
import org.xg.auth.SessionManager;
import org.xg.svc.UserPass;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.BiFunction;
import java.util.logging.Logger;


@Path("auth")
public class AuthOps {

  private final static Logger logger = Logger.getLogger(AuthOps.class.getName());

  private static Response authenticate(
    String userPassPostJson,
    BiFunction<String, String, Boolean> authenticator
  ) {
    try {
      UserPass up = UserPass.fromJson(userPassPostJson);
      logger.warning(String.format("Authenticating %s with %s", up.uid(), up.passHashStr()));

      boolean authenticated = authenticator.apply(up.uid(), up.passHashStr()); // UserDbAuthority.authenticateCustomer(up.uid(), up.passHashStr());


      if (authenticated) {
        AuthResp resp = AuthResp.authSuccess(up);
        SessionManager.addSession(up.uid(), resp.token());
        logger.warning(
          String.format("Session added: [%s]-[%s]", up.uid(), resp.token())
        );
        return Response.ok(AuthResp.toJson(resp)).build();
      } else {
        logger.warning("====================== Failed to authenticateCustomer user: ");
        return Response.status(Response.Status.UNAUTHORIZED)
          .entity(String.format("user [%s] NOT authorized!", up.uid()))
          .build();
      }
    }
    catch (Exception ex) {
      logger.warning("Exception while authenticating user: " + userPassPostJson);
      ex.printStackTrace();
      throw new WebApplicationException("====================== Failed to authenticateCustomer user", ex);
    }
  }

  @POST
  @Path("customerPass")
  @Consumes(MediaType.TEXT_PLAIN)
  public Response authorize(String userPassPostJson) {
    return authenticate(
      userPassPostJson,
      UserDbAuthority::authenticateCustomer
    );
//    try {
//      UserPass up = UserPass.fromJson(userPassPostJson);
//
//      logger.warning(String.format("Authenticating %s with %s", up.uid(), up.passHashStr()));
//
//      boolean authenticated = UserDbAuthority.authenticateCustomer(up.uid(), up.passHashStr());
//
//      if (authenticated) {
//        AuthResp resp = AuthResp.authSuccess(up);
//        SessionManager.addSession(up.uid(), resp.token());
//        logger.warning(
//          String.format("Session added: [%s]-[%s]", up.uid(), resp.token())
//        );
//        return Response.ok(AuthResp.toJson(resp)).build();
//      }
//      else {
//        logger.warning("====================== Failed to authenticateCustomer user: " );
//        return Response.status(Response.Status.UNAUTHORIZED)
//          .entity(String.format("user [%s] NOT authorized!", up.uid()))
//          .build();
//      }
//    }
//    catch (Exception ex) {
//      logger.warning("Exception while authenticating user.");
//      ex.printStackTrace();
//      throw new WebApplicationException("====================== Failed to authenticateCustomer user", ex);
//    }
  }

  @POST
  @Path("profPass")
  @Consumes(MediaType.TEXT_PLAIN)
  public Response authorizeProf(String userPassPostJson) {
    try {
      UserPass up = UserPass.fromJson(userPassPostJson);

      logger.warning(String.format("Authenticating %s with %s", up.uid(), up.passHashStr()));

      boolean authenticated = UserDbAuthority.authenticateMedProfs(up.uid(), up.passHashStr());

      if (authenticated) {
        AuthResp resp = AuthResp.authSuccess(up);
        SessionManager.addSession(up.uid(), resp.token());
        logger.warning(
          String.format("Session added: [%s]-[%s]", up.uid(), resp.token())
        );
        return Response.ok(AuthResp.toJson(resp)).build();
      }
      else {
        logger.warning("====================== Failed to authorizeProf user: " );
        return Response.status(Response.Status.UNAUTHORIZED)
          .entity(String.format("user [%s] NOT authorized!", up.uid()))
          .build();
      }
    }
    catch (Exception ex) {
      logger.warning("Exception while authenticating user.");
      ex.printStackTrace();
      throw new WebApplicationException("====================== Failed to authorizeProf user", ex);
    }
  }

  @POST
  @Path("orgsPass")
  @Consumes(MediaType.TEXT_PLAIN)
  public Response authorizeOrg(String userPassPostJson) {
    try {
      UserPass up = UserPass.fromJson(userPassPostJson);

      logger.warning(String.format("Authenticating %s with %s", up.uid(), up.passHashStr()));

      boolean authenticated = UserDbAuthority.authenticateProfOrgAgent(up.uid(), up.passHashStr());

      if (authenticated) {
        AuthResp resp = AuthResp.authSuccess(up);
        SessionManager.addSession(up.uid(), resp.token());
        logger.warning(
          String.format("Session added: [%s]-[%s]", up.uid(), resp.token())
        );
        return Response.ok(AuthResp.toJson(resp)).build();
      }
      else {
        logger.warning("====================== Failed to authenticateProfOrgAgent user: " );
        return Response.status(Response.Status.UNAUTHORIZED)
          .entity(String.format("user [%s] NOT authorized!", up.uid()))
          .build();
      }
    }
    catch (Exception ex) {
      logger.warning("Exception while authenticating user.");
      ex.printStackTrace();
      throw new WebApplicationException("====================== Failed to authenticateProfOrgAgent user", ex);
    }
  }

}
