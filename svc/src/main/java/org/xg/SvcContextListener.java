package org.xg;

import org.apache.commons.io.IOUtils;
import org.xg.gnl.GlobalCfg;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SvcContextListener implements ServletContextListener {

  private static GlobalCfg _cfg = null;
  private static Object _lock = new Object();

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    if (_cfg == null) {
      synchronized (_lock) {
        try {
          String path = sce.getServletContext().getRealPath("/WEB-INF/svc_cfg.json");
          String cfgJson = IOUtils.toString(
            new FileInputStream(path),
            StandardCharsets.UTF_8
          );
          _cfg = GlobalCfg.fromJson(cfgJson);
          System.out.println("Configuration initialized!");
        }
        catch (Exception ex) {
          ex.printStackTrace();
          throw new RuntimeException("Error loading config", ex);
        }
      }
    }
  }

  static GlobalCfg getCfg() {
    return _cfg;
  }

//  @Override
//  public void contextDestroyed(ServletContextEvent sce) {
//    System.out.println(
//      "destroying context: " +
//        sce.getServletContext()
//    );
//  }
}
