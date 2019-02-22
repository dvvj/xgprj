package org.xg.auth;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
  private static final String AUTH_SCHEME = "Bearer";

  @Override
  public void filter(ContainerRequestContext reqContext) throws IOException {

    String authHeader = reqContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    System.out.println("Auth header: " + authHeader);

    String token = SvcHelpers.decodeAuthHeader(authHeader);

    if (!tokenChecked(token))
      abortWithUnauthorized(reqContext);

    String scheme = reqContext.getUriInfo().getRequestUri().getScheme();
    reqContext.setSecurityContext(
      new SecContext(
        new SecContextUser("customer1", Arrays.asList("user")),
        scheme
      )
    );
  }

  private static boolean tokenChecked(String token) {
    return token != null && !token.isEmpty();
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
