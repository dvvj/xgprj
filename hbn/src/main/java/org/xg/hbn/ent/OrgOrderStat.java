package org.xg.hbn.ent;

import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name="org_order_stats")
@Immutable
public class OrgOrderStat {
  @Column(name="org_agent_id", length = 255, nullable = false)
  private String orgAgentId;

  @Column(name="prof_id", length = 255, nullable = false)
  private String profId;

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

  @Column(name="org_id", length = 255, nullable = false)
  private String orgId;

  public OrgOrderStat() { }

  public OrgOrderStat(
    String orgAgentId,
    String profId,
    Long orderId,
    Integer productId,
    double qty,
    double actualCost,
    ZonedDateTime creationTime,
    Integer status
  ) {
    this.orgAgentId = orgAgentId;
    this.profId = profId;
    this.orderId = orderId;
    this.productId = productId;
    this.qty = qty;
    this.actualCost = actualCost;
    this.creationTime = creationTime;
    this.status = status;
  }

  public String getOrgAgentId() {
    return orgAgentId;
  }

  public String getProfId() {
    return profId;
  }

  public Long getOrderId() {
    return orderId;
  }

  public Integer getProductId() {
    return productId;
  }

  public double getQty() {
    return qty;
  }

  public double getActualCost() {
    return actualCost;
  }

  public ZonedDateTime getCreationTime() {
    return creationTime;
  }

  public Integer getStatus() {
    return status;
  }

  public String getOrgId() {
    return orgId;
  }
}
