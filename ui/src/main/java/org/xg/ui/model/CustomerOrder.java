package org.xg.ui.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.MOrder;
import org.xg.ui.utils.Global;

import java.util.Map;

public class CustomerOrder extends RecursiveTreeObject<CustomerOrder> {
  private String customerId;
  private String customerName;

  private Order order;

  public CustomerOrder(
    String customerId,
    String customerName,
    Order order
  ) {
    this.customerId = customerId;
    this.customerName = customerName;
    this.order = order;
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

  public static CustomerOrder fromMOrder(MOrder order, Map<String, Customer> customerMap) {
    return new CustomerOrder(
      order.uid(),
      customerMap.get(order.uid()).getName(),
      Order.fromMOrder(order, Global.getProductMap())
    );
  }
}
