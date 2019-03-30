package org.xg.ui;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import org.xg.ui.model.*;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;
import org.xg.uiModels.Order;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

import static org.xg.ui.model.TableViewHelper.*;

public class ExistingOrdersCtrl {
  @FXML
  private JFXTreeTableView<Order> tblOrders;

  private ObservableList<Order> ordersCache;

//  private void setupAndFetchExistingOrderTable(ResourceBundle resBundle, Integer filterCode) {
//    tblOrders.getColumns().addAll(
//      TableViewHelper.<Order, String>jfxTableColumnResBundle(
//        "orderTable.prodName",
//        resBundle,
//        300,
//        Order::getProdName
//      ),
//      TableViewHelper.<Order, Double>jfxTableColumnResBundle(
//        "orderTable.qty",
//        resBundle,
//        100,
//        Order::getQty
//      ),
//      TableViewHelper.<Order, ZonedDateTime>jfxTableColumnResBundle(
//        "orderTable.creationTime",
//        resBundle,
//        200,
//        Order::getCreationTime
//      ),
//      TableViewHelper.<Order, String>jfxTableColumnResBundle(
//        "orderTable.status",
//        resBundle,
//        80,
//        Order::getStatusStr
//      ),
//      jfxOrderTableOpsColumn(
//        Global.AllRes.getString("orderTable.action"),
//        80
//      )
//    );
//
//    tblOrders.setColumnResizePolicy(TreeTableView.UNCONSTRAINED_RESIZE_POLICY);
//    refreshOrderList(filterCode);
//  }

//  private void refreshOrderList(Integer filterCode) {
//    tblOrders.setRoot(null);
//    UIHelpers.setPlaceHolder4TreeView(tblOrders, "orderTable.placeHolder");
//
//    Task<ObservableList<Order>> fetchProductsTask = Helpers.uiTaskJ(
//      () -> {
//        try {
//          Thread.sleep(1000);
//          return Global.updateAllOrders();
//        }
//        catch (Exception ex) {
//          Global.loggingTodo(
//            String.format(
//              "Error fetching customer table for [%s]: %s", Global.getCurrUid(), ex.getMessage()
//            )
//          );
//          return null;
//        }
//      },
//      resp -> {
//        if (resp != null) {
//
//          ordersCache = resp.filtered(
//            OrderFilterHelpers.OrderFilterMap.get(filterCode)
//          );
//          TreeItem<Order> root = new RecursiveTreeItem<>(ordersCache, RecursiveTreeObject::getChildren);
//
//          //tblOrders.itemsProperty().bindBidirectional(ordersCache);
//          tblOrders.setRoot(root);
//          tblOrders.setShowRoot(false);
//        }
//        else {
//          // todo: show error
//        }
//        return null;
//      },
//      30000
//    );
//
//    new Thread(fetchProductsTask).start();
//  }

  @FXML
  private JFXComboBox<ComboOptionData> cmboFilter;

//  @PostConstruct
//  public void initialize() {
//    Integer filterCode = OrderFilterHelpers.OF_UNPAID;
//    cmboFilter.getItems().addAll(OrderFilterHelpers.FilterOptionsMap.values());
//    cmboFilter.setValue(OrderFilterHelpers.FilterOptionsMap.get(filterCode));
//    setupAndFetchExistingOrderTable(Global.AllRes, filterCode);
//    cmboFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
//      boolean hasUpdate = newValue != null &&
//        (oldValue == null || !oldValue.getCode().equals(newValue.getCode()));
//
//      if (hasUpdate) {
//        int newVal = newValue.getCode();
//        System.out.println("New selection: " + newVal);
//        refreshOrderList(newVal);
//      }
//    });
//  }
}
