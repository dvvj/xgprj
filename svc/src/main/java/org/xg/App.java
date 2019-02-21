package org.xg;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends ResourceConfig {
  public App() {
    register(
      new LoggingFeature(
        Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
        Level.WARNING,
        LoggingFeature.Verbosity.HEADERS_ONLY,
        10000
      )
    );
    register(RolesAllowedDynamicFeature.class);
  }
}
