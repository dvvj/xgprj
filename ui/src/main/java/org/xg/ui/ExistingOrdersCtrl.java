package org.xg.ui;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
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

import static org.xg.ui.model.ProductTableHelper.*;

public class ExistingOrdersCtrl implements Initializable {
  @FXML
  private TableView<Order> tblOrders;

  private Property<ObservableList<Order>> ordersCache = new SimpleListProperty<>();

  @Override
  public void initialize(URL location, ResourceBundle resBundle) {
    ordersCache.setValue(Global.updateAllOrders());

    tblOrders.itemsProperty().bindBidirectional(ordersCache);

    tblOrders.getColumns().addAll(
      tableColumnResBundle(
        "orderTable.prodName",
        resBundle,
        "prodName",
        300
      ),
      tableColumnResBundle(
        "orderTable.qty",
        resBundle,
        "qty",
        100
      ),
      tableColumnResBundle(
        "orderTable.creationTime",
        resBundle,
        "creationTime",
        200
      ),
      tableColumnResBundle(
        "orderTable.status",
        resBundle,
        "statusStr",
        80
      ),
      orderTableOpsColumn(
        Global.AllRes.getString("orderTable.action"),
        80
      )
    );

  }
}
