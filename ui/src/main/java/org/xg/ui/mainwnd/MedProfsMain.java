package org.xg.ui.mainwnd;

import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.xg.ui.UiLoginController;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
import org.xg.ui.model.Customer;
import org.xg.ui.model.CustomerOrder;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
          Thread.sleep(3000);
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

  @FXML
  VBox tbl1;

  @PostConstruct
  public void launch() {
    System.out.println("in @PostConstruct");
    setupAndFetchCustomerTable();

    setupAndFetchCustomerOrderTable();

    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;
    try {
      //productLoader.setLocation(path);
      table = tableLoader.load();
      TreeTableViewWithFilterCtrl tblCtrl = tableLoader.getController();
      tblCtrl.setup(
        "customerTable.toolbar.heading",
        "customerTable.toolbar.refresh",
        "customerTable.toolbar.searchPrompt",
        "customerTable.toolbar.filter",
        "customerTable.emptyPlaceHolder",
        c -> {
          Customer customer = (Customer)c;
          Set<String> strs = new HashSet<>();
          strs.addAll(Arrays.asList(
            customer.getName(),
            customer.getUid(),
            customer.getMobile()
          ));
          return strs;
        }
      );

      tblCtrl.setupColumsAndLoadData(
        tbl -> {
          JFXTreeTableView<Customer> theTable = (JFXTreeTableView<Customer>)tbl;
          theTable.getColumns().addAll(
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

          UIHelpers.setPlaceHolder4TreeView(theTable, "customerTable.placeHolder");

        },
        () -> Global.updateAllCustomers()
      );

      tbl1.getChildren().addAll(table);
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }
}
