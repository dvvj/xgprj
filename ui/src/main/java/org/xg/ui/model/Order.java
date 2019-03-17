package org.xg.ui.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.MOrder;
import org.xg.gnl.DataUtils;
import org.xg.ui.utils.Global;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class Order extends RecursiveTreeObject<Order> {

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
    return status != OrderStatus_Locked;
  }

  public boolean getNotPayed() {
    return status == OrderStatus_NotPayed;
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
    if (mo.procTime1S().nonEmpty()) {
      return OrderStatus_Locked;
    }
    else if (mo.payTime().nonEmpty()) {
      return OrderStatus_Payed;
    }
    else {
      return OrderStatus_NotPayed;
    }
  }
  private final static int OrderStatus_NotPayed = 1;
  private final static int OrderStatus_Payed = 2;
  private final static int OrderStatus_Locked = 3;

  private final static Map<Integer, String> statusMap;
  static {
    statusMap = new HashMap<>();
    statusMap.put(OrderStatus_NotPayed, Global.AllRes.getString("orderTable.status.orderNotPayed"));
    statusMap.put(OrderStatus_Payed, Global.AllRes.getString("orderTable.status.orderPayed"));
    statusMap.put(OrderStatus_Locked, Global.AllRes.getString("orderTable.status.orderLocked"));
  }

  public static Order fromMOrder(MOrder mo, Map<Integer, Product> productMap) {
    Product product = productMap.get(mo.productId());
    ZonedDateTime dt = DataUtils.utcTimeFromStr(mo.creationTimeS());
    ZonedDateTime pdt =
      mo.payTime().nonEmpty() ? DataUtils.utcTimeFromStr(mo.payTime().get()) : null;
    int status = getOrderStatus(mo);
    return new Order(mo.id(), product.getName(), mo.qty(), dt, pdt, status);
  }
}
