package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.uiModels.CustomerOrder;
import org.xg.uiModels.Order;

public class CustomerDataModel {
  private final ObservableList<Order> orders;

  public CustomerDataModel(Order[] orders) {
    this.orders = FXCollections.observableArrayList(orders);
  }

  public ObservableList<Order> getOrders() {
    return orders;
  }
}
