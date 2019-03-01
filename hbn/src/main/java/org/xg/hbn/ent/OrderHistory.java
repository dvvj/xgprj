package org.xg.hbn.ent;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="order_history")
public class OrderHistory {

  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column(name="order_id", nullable = false)
  private Long orderId;

  @Column(name="update_time", nullable = false)
  private ZonedDateTime updateTime;

  @Column(name="old_qty", nullable = false)
  private double oldQty;

  public OrderHistory() { }

  public OrderHistory(
    Long orderId,
    ZonedDateTime updateTime,
    double oldQty
  ) {
    this.orderId = orderId;
    this.updateTime = updateTime;
    this.oldQty = oldQty;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public ZonedDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(ZonedDateTime updateTime) {
    this.updateTime = updateTime;
  }

  public double getOldQty() {
    return oldQty;
  }

  public void setOldQty(double oldQty) {
    this.oldQty = oldQty;
  }
}
