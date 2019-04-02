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
    BiFunction<String, String, Boolean> authenticator,
    String tag
  ) {
    try {
      UserPass up = UserPass.fromJson(userPassPostJson);
      logger.warning(String.format(tag + " Authenticating %s with %s", up.uid(), up.passHashStr()));

      boolean authenticated = authenticator.apply(up.uid(), up.passHashStr()); // UserDbAuthority.authenticateCustomer(up.uid(), up.passHashStr());


      if (authenticated) {
        AuthResp resp = AuthResp.authSuccess(up);
        SessionManager.addSession(up.uid(), resp.token());
        logger.warning(
          String.format(tag + " Session added: [%s]-[%s]", up.uid(), resp.token())
        );
        return Response.ok(AuthResp.toJson(resp)).build();
      } else {
        logger.warning(tag + " Failed to authenticateCustomer user: ");
        return Response.status(Response.Status.UNAUTHORIZED)
          .entity(String.format("user [%s] NOT authorized!", up.uid()))
          .build();
      }
    }
    catch (Exception ex) {
      logger.warning("Exception while authenticating user: " + userPassPostJson);
      ex.printStackTrace();
      throw new WebApplicationException(tag + " Failed to authenticateCustomer user", ex);
    }
  }

  @POST
  @Path("authCustomer")
  @Consumes(MediaType.TEXT_PLAIN)
  public Response authCustomer(String userPassPostJson) {
    return authenticate(
      userPassPostJson,
      UserDbAuthority::authenticateCustomer,
      "[AuthCustomer]"
    );
  }

  @POST
  @Path("authMedProf")
  @Consumes(MediaType.TEXT_PLAIN)
  public Response authorizeProf(String userPassPostJson) {
    return authenticate(
      userPassPostJson,
      UserDbAuthority::authenticateMedProfs,
      "[AuthMedProf]"
    );
//    try {
//      UserPass up = UserPass.fromJson(userPassPostJson);
//
//      logger.warning(String.format("Authenticating %s with %s", up.uid(), up.passHashStr()));
//
//      boolean authenticated = UserDbAuthority.authenticateMedProfs(up.uid(), up.passHashStr());
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
//        logger.warning("====================== Failed to authorizeProf user: " );
//        return Response.status(Response.Status.UNAUTHORIZED)
//          .entity(String.format("user [%s] NOT authorized!", up.uid()))
//          .build();
//      }
//    }
//    catch (Exception ex) {
//      logger.warning("Exception while authenticating user.");
//      ex.printStackTrace();
//      throw new WebApplicationException("====================== Failed to authorizeProf user", ex);
//    }
  }

  @POST
  @Path("authProfOrgAgent")
  @Consumes(MediaType.TEXT_PLAIN)
  public Response authorizeOrgAgent(String userPassPostJson) {
    return authenticate(
      userPassPostJson,
      UserDbAuthority::authenticateProfOrgAgent,
      "[AuthProfOrgAgent]"
    );
//    try {
//      UserPass up = UserPass.fromJson(userPassPostJson);
//
//      logger.warning(String.format("Authenticating %s with %s", up.uid(), up.passHashStr()));
//
//      boolean authenticated = UserDbAuthority.authenticateProfOrgAgent(up.uid(), up.passHashStr());
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
//        logger.warning("====================== Failed to authenticateProfOrgAgent user: " );
//        return Response.status(Response.Status.UNAUTHORIZED)
//          .entity(String.format("user [%s] NOT authorized!", up.uid()))
//          .build();
//      }
//    }
//    catch (Exception ex) {
//      logger.warning("Exception while authenticating user.");
//      ex.printStackTrace();
//      throw new WebApplicationException("====================== Failed to authenticateProfOrgAgent user", ex);
//    }
  }


  @POST
  @Path("authProfOrg")
  @Consumes(MediaType.TEXT_PLAIN)
  public Response authProfOrg(String userPassPostJson) {
    return authenticate(
      userPassPostJson,
      UserDbAuthority::authenticateProfOrg,
      "[AuthProfOrg]"
    );
  }
}
