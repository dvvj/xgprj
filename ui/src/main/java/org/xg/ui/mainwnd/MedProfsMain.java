package org.xg.ui.mainwnd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;
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
  private TableView tblCustomers;

  private Property<ObservableList<Customer>> customersCache = new SimpleListProperty<>();
//  @Override
//  public void initialize(URL location, ResourceBundle resources) {
//
//    //Global.setSceneDefStyle(mainWnd);
//  }

  @PostConstruct
  public void launch() {
    System.out.println("in @PostConstruct");
    tblCustomers.getColumns().addAll(
      ProductTableHelper.tableColumnResBundle(
        "customerTable.uid",
        Global.AllRes,
        "uid",
        100
      ),
      ProductTableHelper.tableColumnResBundle(
        "customerTable.name",
        Global.AllRes,
        "name",
        100
      ),
      ProductTableHelper.tableColumnResBundle(
        "customerTable.mobile",
        Global.AllRes,
        "mobile",
        100
      )
    );

    customersCache.setValue(Global.updateAllCustomers());
    tblCustomers.itemsProperty().bindBidirectional(customersCache);

    if (customersCache.getValue().size() > 0) {
      tblCustomers.getSelectionModel().select(0);
    }
  }
}
