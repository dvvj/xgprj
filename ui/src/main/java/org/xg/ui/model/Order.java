package org.xg.ui.model;

import org.xg.db.model.MOrder;
import org.xg.gnl.DataUtils;
import org.xg.ui.utils.Global;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;

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

  public static Order fromMOrder(MOrder mo, Map<Integer, Product> productMap) {
    Product product = productMap.get(mo.productId());
    ZonedDateTime dt = LocalDateTime.parse(mo.creationTimeS()).atZone(DataUtils.UTC());
    boolean blocked = mo.procTime1S() != null && !mo.procTime1S().isEmpty();
    return new Order(mo.id(), product.getName(), mo.qty(), dt, blocked);
  }
}
