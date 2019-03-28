package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.MOrder;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.gnl.DataUtils;

import java.time.ZonedDateTime;
import java.util.Map;

public class Order extends RecursiveTreeObject<Order> {

  private Long id;
  private Integer prodId;
  private String prodName;
  private Double qty;
  private ZonedDateTime creationTime;
  private ZonedDateTime payTime;
  private int status;
  private String statusStr;

  public String getStatusStr() {
    return statusStr;
  }

  public boolean getCanBeModified() {
    return status != MOrgOrderStat.Status_Locked();
  }

  public boolean getNotPayed() {
    return status == MOrgOrderStat.Status_CreatedNotPaid();
  }

  public Order() {

  }

  public Order(
    Long id,
    Integer prodId,
    String prodName,
    Double qty,
    ZonedDateTime creationTime,
    ZonedDateTime payTime,
    int status
  ) {
    this.id = id;
    this.prodId = prodId;
    this.prodName = prodName;
    this.qty = qty;
    this.creationTime = creationTime;
    this.payTime = payTime;
    this.status = status;
//    this.statusStr = statusStr;
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

  public boolean isCreatedThisMonth() {
    ZonedDateTime zdtNow = DataUtils.utcTimeNow();
    return zdtNow.getYear() == creationTime.getYear() && zdtNow.getMonthValue() == creationTime.getMonthValue();
  }

  public boolean isCreatedLastMonth() {
    ZonedDateTime zdtLastMonthStart = DataUtils.utcTimeNow().minusMonths(1).plusDays(1);
    return zdtLastMonthStart.compareTo(creationTime) <= 0;
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

  public static Order fromMOrder(MOrder mo, Map<Integer, Product> productMap) {
    Product product = productMap.get(mo.productId());
    ZonedDateTime dt = DataUtils.utcTimeFromStr(mo.creationTimeS());
    ZonedDateTime pdt =
      mo.payTime().nonEmpty() ? DataUtils.utcTimeFromStr(mo.payTime().get()) : null;
    int status = getOrderStatus(mo);
    return new Order(mo.id(), mo.productId(), product.getName(), mo.qty(), dt, pdt, status);
  }

  public Integer getProdId() {
    return prodId;
  }

  public static int getOrderStatus(MOrder mo) {
    if (mo.procTime1S().nonEmpty()) {
      return MOrgOrderStat.Status_Locked();
    }
    else if (mo.payTime().nonEmpty()) {
      return MOrgOrderStat.Status_Paid();
    }
    else {
      return MOrgOrderStat.Status_CreatedNotPaid();
    }
  }
}
