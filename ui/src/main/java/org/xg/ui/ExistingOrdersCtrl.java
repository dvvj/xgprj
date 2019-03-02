package org.xg.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import org.xg.auth.SvcHelpers;
import org.xg.db.model.MOrder;
import org.xg.gnl.GlobalCfg;
import org.xg.ui.model.Order;
import org.xg.ui.model.Product;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ExistingOrdersCtrl implements Initializable {
  @FXML
  private TableView<Order> tblOrders;

  private ObservableList<Order> getOrders(Map<Integer, Product> productMap) {
    GlobalCfg cfg = GlobalCfg.localTestCfg();

    String j = SvcHelpers.get(cfg.currOrdersURL(), Global.getCurrToken());
    MOrder[] morders = MOrder.fromJsons(j);
    Order[] orders = Helpers.convOrders(morders, productMap);
    return FXCollections.observableArrayList(orders);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }
}
