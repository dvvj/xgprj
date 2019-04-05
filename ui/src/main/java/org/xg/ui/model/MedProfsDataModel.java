package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MOrder;
import org.xg.dbModels.MPricePlan;
import org.xg.pay.pricePlan.TPricePlan;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.uiModels.Customer;
import org.xg.uiModels.CustomerOrder;
import org.xg.uiModels.PricePlan;
import org.xg.uiModels.Product;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MedProfsDataModel {

  private ObservableList<Customer> customers;
  private Map<String, Customer> customerMap;
  private Map<String, MPricePlan> customerPricePlanMap;

  private final CustomerOrder[] rawOrders;
  private final ObservableList<CustomerOrder> customerOrders;
  private final TRewardPlan rewardPlan;

  private final ObservableList<PricePlan> pricePlans;
  private final ObservableList<PricePlanOption> pricePlanOptions;

  private final Map<Integer, Product> prodMap;

  public void setCustomers(MCustomer[] customers, Map<String, MPricePlan> customerPricePlanMap) {
    this.customerPricePlanMap = customerPricePlanMap;
    this.customers = FXCollections.observableArrayList(
      Helpers.convCustomers(customers, this.customerPricePlanMap)
    );
    customerMap = this.customers.stream().collect(Collectors.toMap(Customer::getUid, Function.identity()));
  }



  public MedProfsDataModel(
    MCustomer[] customers,
    MOrder[] customerOrders,
    MPricePlan[] pricePlans,
    Map<Integer, Product> prodMap,
    Map<String, MPricePlan> customerPricePlanMap,
    TRewardPlan rewardPlan
  ) {
    Global.loggingTodo("price plans: " + pricePlans.length);
    PricePlan[] plans = Arrays.stream(pricePlans).map(PricePlan::fromM).toArray(PricePlan[]::new);
    this.pricePlans = FXCollections.observableArrayList(plans);
    this.pricePlanOptions = PricePlanOption.pricePlanOptionsIncludingNone(pricePlans);

    setCustomers(customers, customerPricePlanMap);
    rawOrders = Helpers.convCustomerOrders(customerOrders, customerMap, prodMap, rewardPlan);
    this.customerOrders = FXCollections.observableArrayList(rawOrders);
    this.prodMap = prodMap;
    this.rewardPlan = rewardPlan;
  }

  public ObservableList<PricePlan> getPricePlans() {
    return pricePlans;
  }

  public CustomerOrder[] getOrderData() {
    return rawOrders;
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

  public ObservableList<PricePlanOption> getPricePlanOptions() {
    return pricePlanOptions;
  }
}
