package org.xg.ui.utils;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Locale;
import java.util.ResourceBundle;

public class Global {
  public final static Locale locale = new Locale("zh", "CN");

  public static ResourceBundle getBundle(String resName) {
    return ResourceBundle.getBundle(resName, locale, new Utf8ResBundleCtrl());
  }

  public static Scene sceneDefStyle(Parent root) {
    Scene scene = new Scene(root);
    scene.getStylesheets().add(
      Global.class.getResource("/default.css").toExternalForm()
    );
    return scene;
  }
}
