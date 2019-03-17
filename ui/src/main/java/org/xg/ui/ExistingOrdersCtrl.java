package org.xg.ui;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import org.xg.ui.model.Order;
import org.xg.ui.model.Product;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

import static org.xg.ui.model.TableViewHelper.*;
import static org.xg.ui.utils.Global.getAllProducts;

public class ExistingOrdersCtrl implements Initializable {
  @FXML
  private JFXTreeTableView<Order> tblOrders;

  private ObservableList<Order> ordersCache;

  private void setupAndFetchExistingOrderTable(ResourceBundle resBundle) {
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

    tblOrders.setColumnResizePolicy(TreeTableView.UNCONSTRAINED_RESIZE_POLICY);
    UIHelpers.setPlaceHolder4TreeView(tblOrders, "orderTable.placeHolder");

    Task<ObservableList<Order>> fetchProductsTask = Helpers.uiTaskJ(
      () -> {
        try {
          Thread.sleep(1000);
          return Global.updateAllOrders();
        }
        catch (Exception ex) {
          Global.loggingTodo(
            String.format(
              "Error fetching customer table for [%s]: %s", Global.getCurrUid(), ex.getMessage()
            )
          );
          return null;
        }
      },
      resp -> {
        if (resp != null) {
          ordersCache = resp;
          TreeItem<Order> root = new RecursiveTreeItem<>(ordersCache, RecursiveTreeObject::getChildren);

          //tblOrders.itemsProperty().bindBidirectional(ordersCache);
          tblOrders.setRoot(root);
          tblOrders.setShowRoot(false);
        }
        else {
          // todo: show error
        }
        return null;
      },
      30000
    );

    new Thread(fetchProductsTask).start();

  }

  @Override
  public void initialize(URL location, ResourceBundle resBundle) {
    setupAndFetchExistingOrderTable(resBundle);
  }
}
