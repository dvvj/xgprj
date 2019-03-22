package org.xg.hbn.ent;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="org_order_stats")
public class OrgOrderStat {

  @Column(name="org_id", length = 255, nullable = false)
  private String orgId;

  @Column(name="agent_id", length = 255, nullable = false)
  private String agentId;

  @Id
  @Column(name="order_id", nullable = false)
  private Long orderId;

  @Column(name="product_id", nullable = false)
  private Integer productId;

  @Column(name="qty", nullable = false)
  private double qty;

  @Column(name="actual_cost", nullable = false)
  private double actualCost;

  @Column(name="creation_time", nullable = false)
  private ZonedDateTime creationTime;

  @Column(name="status", nullable = false)
  private Integer status;

  public OrgOrderStat() { }

  public OrgOrderStat(
    String orgId,
    String agentId,
    Long orderId,
    Integer productId,
    double qty,
    double actualCost,
    ZonedDateTime creationTime,
    Integer status
  ) {
    this.orgId = orgId;
    this.agentId = agentId;
    this.orderId = orderId;
    this.productId = productId;
    this.qty = qty;
    this.actualCost = actualCost;
    this.creationTime = creationTime;
    this.status = status;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getAgentId() {
    return agentId;
  }

  public void setAgentId(String agentId) {
    this.agentId = agentId;
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

  public double getActualCost() {
    return actualCost;
  }

  public void setActualCost(double actualCost) {
    this.actualCost = actualCost;
  }

  public ZonedDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(ZonedDateTime creationTime) {
    this.creationTime = creationTime;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}
