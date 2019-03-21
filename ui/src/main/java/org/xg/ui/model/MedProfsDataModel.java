package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MOrder;
import org.xg.ui.utils.Helpers;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MedProfsDataModel {

  private final ObservableList<Customer> customers;
  private final ObservableList<CustomerOrder> customerOrders;

  private final Map<String, Customer> customerMap;

  public MedProfsDataModel(
    MCustomer[] customers,
    MOrder[] customerOrders,
    Map<Integer, Product> prodMap
  ) {
    this.customers = FXCollections.observableArrayList(
      Helpers.convCustomers(customers)
    );
    customerMap = this.customers.stream().collect(Collectors.toMap(Customer::getUid, Function.identity()));
    this.customerOrders = FXCollections.observableArrayList(
      Helpers.convCustomerOrders(customerOrders, customerMap, prodMap)
    );
  }

  public ObservableList<Customer> getCustomers() {
    return customers;
  }

  public ObservableList<CustomerOrder> getCustomerOrders() {
    return customerOrders;
  }

  public Customer customerById(String customerId) {
    return customerMap.get(customerId);
  }
}
