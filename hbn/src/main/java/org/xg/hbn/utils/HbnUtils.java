package org.xg.hbn.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.File;

public class HbnUtils {

  private static SessionFactory buildSessFactory(String cfgFile) {
    try {
      ServiceRegistry svcReg = new StandardServiceRegistryBuilder()
        .configure(new File(cfgFile)).build();

      Metadata md = new MetadataSources(svcReg).getMetadataBuilder().build();
      return md.getSessionFactoryBuilder().build();
    }
    catch (Exception ex) {
      throw new RuntimeException("Error in buildSessFactory()", ex);
    }
  }

  private static SessionFactory createTestSessFactory() {
    try {
      ServiceRegistry svcReg = new StandardServiceRegistryBuilder()
        .configure("hibernate.cfg.xml").build();

      Metadata md = new MetadataSources(svcReg).getMetadataBuilder().build();
      return md.getSessionFactoryBuilder().build();
    }
    catch (Exception ex) {
      throw new RuntimeException("Error in buildSessFactory()", ex);
    }
  }

  public final static SessionFactory testSessFactory = createTestSessFactory();

  public static SessionFactory getSessFactory(String cfgFile) {
    return buildSessFactory(cfgFile);
  }

  public static void shutdown(SessionFactory sessFactory) {
    sessFactory.close();
  }

  public static void shutdownTest() {
    testSessFactory.close();
  }


}
