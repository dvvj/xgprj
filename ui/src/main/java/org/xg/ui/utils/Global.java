package org.xg.ui.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.xg.auth.SvcHelpers;
import org.xg.db.model.MProduct;
import org.xg.gnl.GlobalCfg;
import org.xg.ui.model.Product;

import java.util.Locale;
import java.util.ResourceBundle;

public class Global {
  public final static Locale locale = new Locale("zh", "CN");

  public final static ResourceBundle AllRes = ResourceBundle.getBundle("ui.AllRes", locale, new Utf8ResBundleCtrl());

  public static Scene sceneDefStyle(Parent root) {
    Scene scene = new Scene(root);
    scene.getStylesheets().add(
      Global.class.getResource("/default.css").toExternalForm()
    );
    return scene;
  }

  public static void setText(Text txt, String resName, Color color) {
    txt.setText(Global.AllRes.getString(resName));
    txt.setStroke(color);
  }

  private static String _currToken;

  public static void updateToken(String token) {
    _currToken = token;
  }
  public static String getCurrToken() {
    return _currToken;
  }

  private static ObservableList<Product> allProducts = null;
  public static ObservableList<Product> updateAllProducts() {
    try {
      GlobalCfg cfg = GlobalCfg.localTestCfg();
      String j = SvcHelpers.get(
        cfg.allProductsURL(),
        Global.getCurrToken()
      );
      MProduct[] products = MProduct.fromJsons(j);
      Product[] prods = Helpers.convProducts(products);
      return FXCollections.observableArrayList(prods);
    }
    catch (Exception ex) {
      //todo global status in ui
      System.out.println("Cannot retrieve products: " + ex.getMessage());
      ex.printStackTrace();
      return FXCollections.observableArrayList();
    }
  }

  public static ObservableList<Product> getAllProducts() {
    if (allProducts == null || allProducts.isEmpty()) {
      allProducts = updateAllProducts();
    }

    return allProducts;
  }
}
