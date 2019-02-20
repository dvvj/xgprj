package org.xg;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class App extends ResourceConfig {
  public App() {
    register(RolesAllowedDynamicFeature.class);
  }
}
