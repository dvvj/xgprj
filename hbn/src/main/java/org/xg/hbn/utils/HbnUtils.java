package org.xg.hbn.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class HbnUtils {

  private static SessionFactory buildSessFactory() {
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

  public static final SessionFactory sessFactory = buildSessFactory();

  public static void shutdown() {
    sessFactory.close();
  }


}
