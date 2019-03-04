package org.xg.auth;

import org.xg.AuthOps;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
  private static final String AUTH_SCHEME = "Bearer";
  private final static Logger logger = Logger.getLogger(AuthFilter.class.getName());

  @Override
  public void filter(ContainerRequestContext reqContext) throws IOException {

    String authHeader = reqContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    logger.info("Auth header: " + authHeader);

    String token = SvcHelpers.decodeAuthHeader(authHeader);

    if (!tokenChecked(token))
      abortWithUnauthorized(reqContext);

    String scheme = reqContext.getUriInfo().getRequestUri().getScheme();
    String uid = SessionManager.findUid(token);
    reqContext.setSecurityContext(
      new SecContext(
        new SecContextUser(uid, Arrays.asList("user")),
        scheme
      )
    );
  }

  private static boolean tokenChecked(String token) {
    return SessionManager.checkSession(token);
  }

  private static void abortWithUnauthorized(ContainerRequestContext reqContext) {
    reqContext.abortWith(
      Response.status(Response.Status.UNAUTHORIZED)
      .header(
        HttpHeaders.WWW_AUTHENTICATE,
        AUTH_SCHEME + " realm='example'"
      ).build()
    );
  }
}
