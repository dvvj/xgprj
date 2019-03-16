package org.xg.ui.mainwnd;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import org.xg.dbModels.MCustomer;
import org.xg.ui.model.Customer;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;

import javax.annotation.PostConstruct;

@ViewController(value = "/ui/MedProfsMain.fxml")
public class MedProfsMain {

  @FXML
  private HBox mainWnd;

  @FXML
  private JFXTreeTableView tblCustomers;

  @FXML
  private JFXTreeTableView tblOrders;

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

    Label lblPlaceHolder = new Label();
    lblPlaceHolder.setText(
      Global.AllRes.getString("customerTable.placeHolder")
    );
    tblCustomers.setPlaceholder(lblPlaceHolder);

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
          TreeItem<Customer> items = new RecursiveTreeItem<>(customersCache, RecursiveTreeObject::getChildren);
          tblCustomers.setRoot(items);
          tblCustomers.setShowRoot(false);

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

  @PostConstruct
  public void launch() {
    System.out.println("in @PostConstruct");
    setupAndFetchCustomerTable();
  }
}
