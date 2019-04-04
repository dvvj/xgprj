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

  private final CustomerOrder[] rawOrders;
  private final ObservableList<CustomerOrder> customerOrders;
  private final TRewardPlan rewardPlan;

  private final ObservableList<PricePlan> pricePlans;
  private final ObservableList<PricePlanOption> pricePlanOptions;

  private final Map<Integer, Product> prodMap;

  public void setCustomers(MCustomer[] customers) {
    this.customers = FXCollections.observableArrayList(
      Helpers.convCustomers(customers)
    );
    customerMap = this.customers.stream().collect(Collectors.toMap(Customer::getUid, Function.identity()));
  }

  private final static String NoPricePlanId = "c49a2438-fc30-4103-8e83-86f410a31ed4";
  public final static PricePlanOption NoPricePlan = new PricePlanOption(
    NoPricePlanId,
    Global.AllRes.getString("addNewCustomer.pricePlanType.noPricePlan")
  );

  private static PricePlanOption[] createPricePlanOptions(MPricePlan[] pricePlans) {
    PricePlanOption[] planOptions = Arrays.stream(pricePlans).map(p -> new PricePlanOption(p.id(), p.info()))
      .toArray(PricePlanOption[]::new);

    PricePlanOption[] res = new PricePlanOption[planOptions.length+1];
    res[0] = NoPricePlan;
    for (int i = 0; i < planOptions.length; i++) {
      res[i+1] = planOptions[i];
    }
    return res;
  }

  public MedProfsDataModel(
    MCustomer[] customers,
    MOrder[] customerOrders,
    MPricePlan[] pricePlans,
    Map<Integer, Product> prodMap,
    TRewardPlan rewardPlan
  ) {
    Global.loggingTodo("price plans: " + pricePlans.length);
    PricePlan[] plans = Arrays.stream(pricePlans).map(PricePlan::fromM).toArray(PricePlan[]::new);
    PricePlanOption[] planOptions = createPricePlanOptions(pricePlans);
    this.pricePlans = FXCollections.observableArrayList(plans);
    this.pricePlanOptions = FXCollections.observableArrayList(planOptions);
    setCustomers(customers);
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
