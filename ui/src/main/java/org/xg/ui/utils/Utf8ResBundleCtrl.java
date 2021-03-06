package org.xg.ui.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Utf8ResBundleCtrl extends ResourceBundle.Control {

  @Override
  public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
    return _newBundle_OriginalCrap(baseName, locale, format, loader, reload, StandardCharsets.UTF_8);
  }

  private String toResourceName1(String bundleName, String suffix) {
    // application protocol check
    if (bundleName.contains("://")) {
      return null;
    } else {
      return toResourceName(bundleName, suffix);
    }
  }
  private ResourceBundle _newBundle_OriginalCrap(String baseName, Locale locale, String format, ClassLoader loader, boolean reload, Charset encoding)
    throws IllegalAccessException, InstantiationException, IOException {
    String bundleName = toBundleName(baseName, locale);
    ResourceBundle bundle = null;
    if (format.equals("java.class")) {
      try {
        @SuppressWarnings("unchecked")
        Class<? extends ResourceBundle> bundleClass
          = (Class<? extends ResourceBundle>)loader.loadClass(bundleName);

        // If the class isn't a ResourceBundle subclass, throw a
        // ClassCastException.
        if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
          bundle = bundleClass.newInstance();
        } else {
          throw new ClassCastException(bundleClass.getName()
            + " cannot be cast to ResourceBundle");
        }
      } catch (ClassNotFoundException e) {
      }
    } else if (format.equals("java.properties")) {
      final String resourceName = toResourceName1(bundleName, "properties");
      if (resourceName == null) {
        return bundle;
      }
      final ClassLoader classLoader = loader;
      final boolean reloadFlag = reload;
      InputStream stream = null;
      try {
        stream = AccessController.doPrivileged(
          new PrivilegedExceptionAction<InputStream>() {
            public InputStream run() throws IOException {
              InputStream is = null;
              if (reloadFlag) {
                URL url = classLoader.getResource(resourceName);
                if (url != null) {
                  URLConnection connection = url.openConnection();
                  if (connection != null) {
                    // Disable caches to get fresh data for
                    // reloading.
                    connection.setUseCaches(false);
                    is = connection.getInputStream();
                  }
                }
              } else {
                is = classLoader.getResourceAsStream(resourceName);
              }
              return is;
            }
          });
      } catch (PrivilegedActionException e) {
        throw (IOException) e.getException();
      }
      if (stream != null) {
        try {
          // interesting part!!
          Reader reader = new InputStreamReader(stream, encoding);
          bundle = new PropertyResourceBundle(reader);
        } finally {
          stream.close();
        }
      }
    } else {
      throw new IllegalArgumentException("unknown format: " + format);
    }
    return bundle;
  }
}
