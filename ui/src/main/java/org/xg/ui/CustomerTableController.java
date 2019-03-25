package org.xg.ui;

import com.jfoenix.controls.JFXTreeTableView;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.xg.uiModels.Customer;
import org.xg.ui.model.TableViewHelper;

import java.net.URL;
import java.util.ResourceBundle;


public class CustomerTableController implements Initializable {
  @FXML
  private JFXTreeTableView<Customer> tblCustomers;

  private Property<ObservableList<Customer>> customerCache = new SimpleListProperty<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    tblCustomers.getColumns().addAll(
      TableViewHelper.jfxTableColumnResBundle(
        "customerTable.uid",
        resources,
        150,
        Customer::getUid
      ),
      TableViewHelper.jfxTableColumnResBundle(
        "customerTable.name",
        resources,
        150,
        Customer::getName
      ),
      TableViewHelper.jfxTableColumnResBundle(
        "customerTable.mobile",
        resources,
        150,
        Customer::getMobile
      )
    );
  }
}
