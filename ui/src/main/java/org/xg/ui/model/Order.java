package org.xg.ui.model;

import java.time.ZonedDateTime;

public class Order {

  private Long id;

  private String prodName;
  private Double qty;
  private ZonedDateTime creationTime;
  private boolean locked;

  public Order() {

  }

  public Order(
    Long id,
    String prodName,
    Double qty,
    ZonedDateTime creationTime,
    boolean locked
  ) {
    this.id = id;
    this.prodName = prodName;
    this.qty = qty;
    this.creationTime = creationTime;
    this.locked = locked;
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

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }
}
