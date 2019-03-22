package org.xg.hbn.ent;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="orders")
public class Order {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="customer_id", length = 255, nullable = false)
  private String customerId;

  @Column(name="product_id", nullable = false)
  private Integer productId;

  @Column(name="qty", nullable = false)
  private double qty;

  @Column(name="actual_cost", nullable = false)
  private double actualCost;

  @Column(name="creation_time", nullable = false)
  private ZonedDateTime creationTime;

  @Column(name="pay_time")
  private ZonedDateTime payTime;

  public ZonedDateTime getPayTime() {
    return payTime;
  }

  public void setPayTime(ZonedDateTime payTime) {
    this.payTime = payTime;
  }

  @Column(name="proc_time1")
  private ZonedDateTime procTime1;

  @Column(name="proc_time2")
  private ZonedDateTime procTime2;

  @Column(name="proc_time3")
  private ZonedDateTime procTime3;

  public Order() { }

  public Order(
    //Long id,
    String customerId,
    Integer productId,
    double qty,
    double actualCost,
    ZonedDateTime creationTime,
    ZonedDateTime procTime1,
    ZonedDateTime procTime2,
    ZonedDateTime procTime3
  ) {
    //this.id = id;
    this.customerId = customerId;
    this.productId = productId;
    this.qty = qty;
    this.actualCost = actualCost;
    this.creationTime = creationTime;
    this.procTime1 = procTime1;
    this.procTime2 = procTime2;
    this.procTime3 = procTime3;
  }

  public Order(
    //Long id,
    String customerId,
    Integer productId,
    double qty,
    double actualCost,
    ZonedDateTime creationTime
  ) {
    //this.id = id;
    this(customerId, productId, qty, actualCost, creationTime, null, null, null);
  }


  public Long getId() {
    return id;
  }

//  public void setId(Long id) {
//    this.id = id;
//  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public double getQty() {
    return qty;
  }

  public void setQty(double qty) {
    this.qty = qty;
  }

  public ZonedDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(ZonedDateTime creationTime) {
    this.creationTime = creationTime;
  }

  public ZonedDateTime getProcTime1() {
    return procTime1;
  }

  public void setProcTime1(ZonedDateTime procTime1) {
    this.procTime1 = procTime1;
  }

  public ZonedDateTime getProcTime2() {
    return procTime2;
  }

  public void setProcTime2(ZonedDateTime procTime2) {
    this.procTime2 = procTime2;
  }

  public ZonedDateTime getProcTime3() {
    return procTime3;
  }

  public void setProcTime3(ZonedDateTime procTime3) {
    this.procTime3 = procTime3;
  }

  public double getActualCost() {
    return actualCost;
  }

  public void setActualCost(double actualCost) {
    this.actualCost = actualCost;
  }
}
