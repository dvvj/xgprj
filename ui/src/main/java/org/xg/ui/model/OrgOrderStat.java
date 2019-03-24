package org.xg.ui.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class OrgOrderStat extends RecursiveTreeObject<OrgOrderStat> {
  private String orgId;
  private String profId;
  private String productId;
  private double qty;
  private double actualCost;
  private String creationTimeS;

  public OrgOrderStat() { }

  public OrgOrderStat(
    String orgId,
    String profId,
    String productId,
    double qty,
    double actualCost,
    String creationTimeS
  ) {
    this.orgId = orgId;
    this.profId = profId;
    this.productId = productId;
    this.qty = qty;
    this.actualCost = actualCost;
    this.creationTimeS = creationTimeS;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getProfId() {
    return profId;
  }

  public void setProfId(String profId) {
    this.profId = profId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
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

  public String getCreationTimeS() {
    return creationTimeS;
  }

  public void setCreationTimeS(String creationTimeS) {
    this.creationTimeS = creationTimeS;
  }
}
