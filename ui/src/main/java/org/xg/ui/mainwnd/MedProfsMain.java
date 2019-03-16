package org.xg.ui.mainwnd;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.xg.ui.UiLoginController;
import org.xg.ui.model.Customer;
import org.xg.ui.model.Product;
import org.xg.ui.model.ProductTableHelper;
import org.xg.ui.utils.Global;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

  @PostConstruct
  public void launch() {
    System.out.println("in @PostConstruct");
    tblCustomers.getColumns().addAll(
      ProductTableHelper.jfxTableColumnResBundle(
        "customerTable.uid",
        Global.AllRes,
        150,
        Customer::getUid
      ),
      ProductTableHelper.jfxTableColumnResBundle(
        "customerTable.name",
        Global.AllRes,
        150,
        Customer::getName
      ),
      ProductTableHelper.jfxTableColumnResBundle(
        "customerTable.mobile",
        Global.AllRes,
        150,
        Customer::getMobile
      )
    );

    customersCache = Global.updateAllCustomers();
    TreeItem<Customer> items = new RecursiveTreeItem<>(customersCache, RecursiveTreeObject::getChildren);
    tblCustomers.setRoot(items);
    tblCustomers.setShowRoot(false);

    if (customersCache.size() > 0) {
      tblCustomers.getSelectionModel().select(0);
    }
  }
}
