package org.xg.ui.mainwnd;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.xg.dbModels.MCustomer;
import org.xg.ui.model.Customer;
import org.xg.ui.model.CustomerOrder;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;

import javax.annotation.PostConstruct;

@ViewController(value = "/ui/MedProfsMain.fxml")
public class MedProfsMain {

  @FXML
  private HBox mainWnd;

  @FXML
  private JFXTreeTableView tblCustomers;

  private ObservableList<Customer> customersCache;

//  @Override
//  public void initialize(URL location, ResourceBundle resources) {
//
//    //Global.setSceneDefStyle(mainWnd);
//  }

  private void setupAndFetchCustomerTable() {
    tblCustomers.getColumns().addAll(
      TableViewHelper.jfxTableColumnResBundle(
        "customerTable.uid",
        Global.AllRes,
        150,
        Customer::getUid
      ),
      TableViewHelper.jfxTableColumnResBundle(
        "customerTable.name",
        Global.AllRes,
        150,
        Customer::getName
      ),
      TableViewHelper.jfxTableColumnResBundle(
        "customerTable.mobile",
        Global.AllRes,
        150,
        Customer::getMobile
      )
    );

    UIHelpers.setPlaceHolder4TreeView(tblCustomers, "customerTable.placeHolder");

    Task<ObservableList<Customer>> fetchCustomersTask = Helpers.uiTaskJ(
      () -> {
        try {
          Thread.sleep(5000);
          return Global.updateAllCustomers();
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
          customersCache = resp;
          UIHelpers.setRoot4TreeView(tblCustomers, customersCache);

          if (customersCache.size() > 0) {
            tblCustomers.getSelectionModel().select(0);
          }
        }
        else {
          // todo: show error
        }
        return null;
      },
      30000
    );

    new Thread(fetchCustomersTask).start();

  }

  private ObservableList<CustomerOrder> customerOrdersCache;

  @FXML
  private JFXTreeTableView tblRefedCustomerOrders;

  private void setupAndFetchCustomerOrderTable() {
    tblRefedCustomerOrders.getColumns().addAll(
      TableViewHelper.<CustomerOrder, String>jfxTableColumnResBundle(
        "refedCustomerOrderTable.customerId",
        Global.AllRes,
        150,
        CustomerOrder::getCustomerId
      ),
      TableViewHelper.<CustomerOrder, String>jfxTableColumnResBundle(
        "refedCustomerOrderTable.productName",
        Global.AllRes,
        150,
        (CustomerOrder co) -> co.getOrder().getProdName()
      ),
      TableViewHelper.<CustomerOrder, Double>jfxTableColumnResBundle(
        "refedCustomerOrderTable.productQty",
        Global.AllRes,
        150,
        (CustomerOrder co) -> co.getOrder().getQty()
      )
    );

    UIHelpers.setPlaceHolder4TreeView(tblRefedCustomerOrders, "refedCustomerOrderTable.placeHolder");

    Task<ObservableList<CustomerOrder>> fetchCustomersTask = Helpers.uiTaskJ(
      () -> {
        try {
          // Thread.sleep(5000);
          return Global.updateAllOrdersOfRefedCustomers();
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
          customerOrdersCache = resp;
          UIHelpers.setRoot4TreeView(tblRefedCustomerOrders, customerOrdersCache);

          if (customerOrdersCache.size() > 0) {
            tblRefedCustomerOrders.getSelectionModel().select(0);
          }
        }
        else {
          // todo: show error
        }
        return null;
      },
      30000
    );

    new Thread(fetchCustomersTask).start();
  }

  @PostConstruct
  public void launch() {
    System.out.println("in @PostConstruct");
    setupAndFetchCustomerTable();

    setupAndFetchCustomerOrderTable();
  }
}
