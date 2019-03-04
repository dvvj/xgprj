package org.xg;

import org.apache.commons.io.IOUtils;
import org.hibernate.SessionFactory;
import org.xg.db.api.TDbOps;
import org.xg.gnl.GlobalCfg;
import org.xg.hbn.HbnDbOpsImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class SvcContextListener implements ServletContextListener {

  private final static Logger logger = Logger.getLogger(SvcContextListener.class.getName());

  private static GlobalCfg _cfg = null;
  private static TDbOps _hbnOps = null;
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
          logger.info("Configuration initialized: " + _cfg);

          String hbnCfgPath =  sce.getServletContext().getRealPath("/WEB-INF/hibernate.cfg.xml");
          _hbnOps = HbnDbOpsImpl.hbnOps(hbnCfgPath);
          logger.info("Hibernate session factory created!");
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

  static TDbOps getDbOps() {
    return _hbnOps;
  }

//  @Override
//  public void contextDestroyed(ServletContextEvent sce) {
//    System.out.println(
//      "destroying context: " +
//        sce.getServletContext()
//    );
//  }
}
