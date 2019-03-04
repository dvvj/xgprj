package org.xg.ui.model;

import javafx.beans.property.IntegerProperty;
import org.xg.db.model.MOrder;
import org.xg.gnl.DataUtils;
import org.xg.ui.utils.Global;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class Order {

  private Long id;

  private String prodName;
  private Double qty;
  private ZonedDateTime creationTime;
  private ZonedDateTime payTime;
  private int status;

  public String getStatusStr() {
    return statusMap.get(status);
  }

  public boolean getCanBeModified() {
    return status == OrderStatus_Created;
  }

  public Order() {

  }

  public Order(
    Long id,
    String prodName,
    Double qty,
    ZonedDateTime creationTime,
    ZonedDateTime payTime,
    int status
  ) {
    this.id = id;
    this.prodName = prodName;
    this.qty = qty;
    this.creationTime = creationTime;
    this.payTime = payTime;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getQty() {
    return qty;
  }

  public void setQty(Double qty) {
    this.qty = qty;
  }

  public ZonedDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(ZonedDateTime creationTime) {
    this.creationTime = creationTime;
  }

  public String getProdName() {
    return prodName;
  }

  public void setProdName(String prodName) {
    this.prodName = prodName;
  }

  public static int getOrderStatus(MOrder mo) {
    if (mo.procTime1S() != null && !mo.procTime1S().isEmpty()) {
      return OrderStatus_Locked;
    }
    else {
      return OrderStatus_Created;
    }
  }
  private final static int OrderStatus_Created = 1;
  private final static int OrderStatus_Locked = 2;

  private final static Map<Integer, String> statusMap;
  static {
    statusMap = new HashMap<>();
    statusMap.put(OrderStatus_Created, Global.AllRes.getString("orderTable.status.orderCreated"));
    statusMap.put(OrderStatus_Locked, Global.AllRes.getString("orderTable.status.orderLocked"));
  }


  public static Order fromMOrder(MOrder mo, Map<Integer, Product> productMap) {
    Product product = productMap.get(mo.productId());
    ZonedDateTime dt = LocalDateTime.parse(mo.creationTimeS()).atZone(DataUtils.UTC());
    ZonedDateTime pdt = LocalDateTime.parse(mo.payTime()).atZone(DataUtils.UTC());
    int status = getOrderStatus(mo);
    return new Order(mo.id(), product.getName(), mo.qty(), dt, pdt, status);
  }
}
