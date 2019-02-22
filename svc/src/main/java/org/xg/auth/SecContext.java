package org.xg.auth;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class SecContext implements SecurityContext {

  private final SecContextUser user;
  private final String scheme;

  public SecContextUser getUser() {
    return user;
  }

  public SecContext(SecContextUser user, String scheme) {
    this.user = user;
    this.scheme = scheme.toLowerCase();
  }

  @Override
  public Principal getUserPrincipal() {
    return user;
  }

  @Override
  public boolean isUserInRole(String role) {
    return user.hasRole(role);
  }

  @Override
  public boolean isSecure() {
    return scheme.equals("https");
  }

  @Override
  public String getAuthenticationScheme() {
    return SecContext.BASIC_AUTH;
  }

  @Override
  public String toString() {
    return String.format(
      "Security Context:" +
        "\n\tUserPrincipal: %s" +
        "\n\t       Scheme: %s" +
        "\n\t   AuthScheme: %s",
      getUserPrincipal(), scheme, getAuthenticationScheme()
    );
  }
}
