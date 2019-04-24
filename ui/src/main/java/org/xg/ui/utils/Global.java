package org.xg.ui.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.xg.auth.SvcHelpers;
import org.xg.dbModels.MProduct;
import org.xg.gnl.GlobalCfg;
import org.xg.pay.pricePlan.TPricePlan;
import org.xg.uiModels.UIProduct;

import java.util.*;

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

  public static void setSceneDefStyle(Parent root) {
    root.getScene().getStylesheets().add(
      Global.class.getResource("/default.css").toExternalForm()
    );
  }

  public static void setResText(Text txt, String resName, Color color) {
    setText(txt, Global.AllRes.getString(resName), color);
  }
  public static void setResText(Text txt, String resName) {
    setText(txt, Global.AllRes.getString(resName), Color.BLACK);
  }
  public static void setText(Text txt, String msg, Color color) {
    txt.setText(msg);
    txt.setStroke(color);
  }

  private static String _currToken;
  private static String _uid;

  public static void updateUidToken(String uid, String token) {
    _currToken = token;
    _uid = uid;
  }
  public static String getCurrToken() {
    return _currToken;
  }
  public static String getCurrUid() {
    return _uid;
  }

  public static void loggingTodo(String msg) {
    System.out.println(msg);
  }

  private static TPricePlan _pricePlan;
  public static void setPricePlan(TPricePlan pricePlan) {
    _pricePlan = pricePlan;
  }
  public static TPricePlan getPricePlan() {
    return _pricePlan;
  }

  private static ObservableList<UIProduct> allProducts = null;
  private static Map<Integer, UIProduct> productMap = null;
  private static Object _productLock = new Object();
  public static Map<Integer, UIProduct> getProductMap() {
    if (productMap == null) {
      synchronized (_productLock) {
        if (productMap == null) {
          updateAllProducts();
        }
      }
    }
    return productMap;
  }
  public static ObservableList<UIProduct> updateAllProducts() {
    try {
      GlobalCfg cfg = UISvcHelpers.serverCfg();
      String j = SvcHelpers.get(
        cfg.allProductsURL(),
        Global.getCurrToken()
      );
      MProduct[] products = MProduct.fromJsons(j);
      UIProduct[] prods = Helpers.convProducts(products);
      productMap = Helpers.productMapFromJ(prods);
      return FXCollections.observableArrayList(prods);
    }
    catch (Exception ex) {
      //todo global status in ui
      System.out.println("Cannot retrieve products: " + ex.getMessage());
      ex.printStackTrace();
      return FXCollections.observableArrayList();
    }
  }

  public static ObservableList<UIProduct> getAllProducts() {
    if (allProducts == null || allProducts.isEmpty()) {
      allProducts = updateAllProducts();
    }

    return allProducts;
  }

//  private static ObservableList<Order> allOrders = null;
//  private static Order[] getAllOrders() {
//    GlobalCfg cfg = UISvcHelpers.serverCfg();
//
//    String j = SvcHelpers.get(cfg.currOrdersURL(), Global.getCurrToken());
//    MOrder[] morders = MOrder.fromJsons(j);
//    Order[] orders = Helpers.convOrders(morders, Global.getProductMap());
//    return orders; //FXCollections.observableArrayList(orders);
//  }


//  //todo, customize cfg
//  private static ClientCfg clientCfg = new ClientCfg(
//    new UserCfg("customer1", 0, ""),
//    GlobalCfg.localTestCfg() //todo
//  );
//
//  public static GlobalCfg getServerCfg() {
//    return clientCfg.serverCfg();
//  }

//  public static MCustomer[] updateAllRefedCustomers() {
//    try {
//      GlobalCfg cfg = getServerCfg();
//      String mcustomersJson = SvcHelpers.post(
//        cfg.customersRefedByURL(),
//        Global.getCurrToken(),
//        getCurrUid()
//      );
//
////      Customer[] customers = Helpers.convCustomers(
////        MCustomer.fromJsons(mcustomersJson)
////      );
//      return MCustomer.fromJsons(mcustomersJson);
//    }
//    catch (Exception ex) {
//      //todo global status in ui
//      System.out.println(
//        String.format("Cannot retrieve customer for [%s]: %s", getCurrUid(), ex.getMessage())
//      );
//      ex.printStackTrace();
//      return new MCustomer[0];
//    }
//  }

//  public static MOrder[] updateAllOrdersOfRefedCustomers() {
//    try {
//      GlobalCfg cfg = getServerCfg();
//      String mordersJson = SvcHelpers.post(
//        cfg.refedCustomerOrdersURL(),
//        Global.getCurrToken(),
//        getCurrUid()
//      );
//
////      CustomerOrder[] corders = Helpers.convCustomerOrders(
////        ,
////        Global.getProductMap() // todo: updated product map (e.g. newly-added / deleted product)
////      );
//      return MOrder.fromJsons(mordersJson);
//    }
//    catch (Exception ex) {
//      //todo global status in ui
//      System.out.println(
//        String.format("Cannot retrieve customer for [%s]: %s", getCurrUid(), ex.getMessage())
//      );
//      ex.printStackTrace();
//      return new MOrder[0];
//    }
//  }

}
