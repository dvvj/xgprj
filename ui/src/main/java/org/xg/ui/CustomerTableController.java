package org.xg.ui;

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
  private TableView<Customer> tblCustomers;

  private Property<ObservableList<Customer>> customerCache = new SimpleListProperty<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    tblCustomers.getColumns().addAll(
      ProductTableHelper.tableColumnResBundle(
        "customerTable.uid",
        resources,
        "uid",
        100
      ),
      ProductTableHelper.tableColumnResBundle(
        "customerTable.name",
        resources,
        "name",
        100
      ),
      ProductTableHelper.tableColumnResBundle(
        "customerTable.mobile",
        resources,
        "mobile",
        100
      )
    );
  }
}
