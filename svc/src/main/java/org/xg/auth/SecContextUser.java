package org.xg.auth;

import java.security.Principal;
import java.util.List;

public class SecContextUser implements Principal {
  private final String uid;
  private final List<String> roles;

  public SecContextUser(String uid, List<String> roles) {
    this.uid = uid;
    this.roles = roles;
  }
//
//  public String getUid() {
//    return uid;
//  }

  @Override
  public String getName() {
    return uid;
  }

  public boolean hasRole(String role) {
    return roles.contains(role);
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    roles.forEach(r -> b.append(r + ','));
    return String.format(
      "%s[%s]", uid, b
    );
  }
}
