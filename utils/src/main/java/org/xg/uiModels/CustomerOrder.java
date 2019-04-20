package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.MOrder;
import org.xg.gnl.DataUtils;
import org.xg.pay.rewardPlan.TRewardPlan;

import java.util.Map;

public class CustomerOrder extends RecursiveTreeObject<CustomerOrder> {
  private String customerId;
  private String customerName;

  private Order order;
  private Double reward;

  public CustomerOrder(
    String customerId,
    String customerName,
    Order order,
    Double reward
  ) {
    this.customerId = customerId;
    this.customerName = customerName;
    this.order = order;
    this.reward = DataUtils.roundMoney(reward);
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Double getReward() {
    return reward;
  }

  public void setReward(Double reward) {
    this.reward = reward;
  }

  public static CustomerOrder fromMOrder(
    MOrder order,
    Map<String, Customer> customerMap,
    Map<Integer, UIProduct> productMap,
    TRewardPlan rewardPlan
  ) {
    Order o = Order.fromMOrder(order, productMap);
    UIProduct prod = productMap.get(o.getProdId());
    Double reward =
      rewardPlan.reward(order.productId(), prod.getPrice0());
    return new CustomerOrder(
      order.uid(),
      customerMap.get(order.uid()).getName(),
      o,
      reward
    );
  }
}
