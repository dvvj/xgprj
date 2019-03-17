package org.xg.ui;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import org.xg.ui.model.Order;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

import static org.xg.ui.model.TableViewHelper.*;

public class ExistingOrdersCtrl implements Initializable {
  @FXML
  private JFXTreeTableView<Order> tblOrders;

  private ObservableList<Order> ordersCache;

  @Override
  public void initialize(URL location, ResourceBundle resBundle) {
    ordersCache = Global.updateAllOrders();
    TreeItem<Order> root = new RecursiveTreeItem<>(ordersCache, RecursiveTreeObject::getChildren);

    //tblOrders.itemsProperty().bindBidirectional(ordersCache);
    tblOrders.setRoot(root);
    tblOrders.setShowRoot(false);

    tblOrders.getColumns().addAll(
      TableViewHelper.<Order, String>jfxTableColumnResBundle(
        "orderTable.prodName",
        resBundle,
        300,
        Order::getProdName
      ),
      TableViewHelper.<Order, Double>jfxTableColumnResBundle(
        "orderTable.qty",
        resBundle,
        100,
        Order::getQty
      ),
      TableViewHelper.<Order, ZonedDateTime>jfxTableColumnResBundle(
        "orderTable.creationTime",
        resBundle,
        200,
        Order::getCreationTime
      ),
      TableViewHelper.<Order, String>jfxTableColumnResBundle(
        "orderTable.status",
        resBundle,
        80,
        Order::getStatusStr
      ),
      jfxOrderTableOpsColumn(
        Global.AllRes.getString("orderTable.action"),
        80
      )
    );

  }
}
