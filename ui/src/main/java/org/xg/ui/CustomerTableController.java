package org.xg.ui;

import com.jfoenix.controls.JFXTreeTableView;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import org.xg.ui.model.Customer;
import org.xg.ui.model.Product;
import org.xg.ui.model.ProductTableHelper;

import java.net.URL;
import java.util.ResourceBundle;


public class CustomerTableController implements Initializable {
  @FXML
  private JFXTreeTableView<Customer> tblCustomers;

  private Property<ObservableList<Customer>> customerCache = new SimpleListProperty<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    tblCustomers.getColumns().addAll(
      ProductTableHelper.jfxTableColumnResBundle(
        "customerTable.uid",
        resources,
        150,
        Customer::getUid
      ),
      ProductTableHelper.jfxTableColumnResBundle(
        "customerTable.name",
        resources,
        150,
        Customer::getName
      ),
      ProductTableHelper.jfxTableColumnResBundle(
        "customerTable.mobile",
        resources,
        150,
        Customer::getMobile
      )
    );
  }
}
