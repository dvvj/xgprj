package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MOrder;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.utils.Helpers;
import org.xg.uiModels.Customer;
import org.xg.uiModels.CustomerOrder;
import org.xg.uiModels.Product;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MedProfsDataModel {

  private final ObservableList<Customer> customers;
  private final CustomerOrder[] rawOrders;
  private final ObservableList<CustomerOrder> customerOrders;
  private final TRewardPlan rewardPlan;

  private final Map<String, Customer> customerMap;
  private final Map<Integer, Product> prodMap;

  public MedProfsDataModel(
    MCustomer[] customers,
    MOrder[] customerOrders,
    Map<Integer, Product> prodMap,
    TRewardPlan rewardPlan
  ) {
    this.customers = FXCollections.observableArrayList(
      Helpers.convCustomers(customers)
    );
    customerMap = this.customers.stream().collect(Collectors.toMap(Customer::getUid, Function.identity()));
    rawOrders = Helpers.convCustomerOrders(customerOrders, customerMap, prodMap, rewardPlan);
    this.customerOrders = FXCollections.observableArrayList(rawOrders);
    this.prodMap = prodMap;
    this.rewardPlan = rewardPlan;
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

  public double calcTotalReward() {
    if (rewardPlan != null)
      return Helpers.calcRewards(rewardPlan, rawOrders, prodMap);
    else
      return 0.0;
  }
}
