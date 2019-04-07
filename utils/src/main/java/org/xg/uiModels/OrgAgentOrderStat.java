package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.busiLogic.OrderStatusLogics;
import org.xg.dbModels.MOrgAgentOrderStat;
import org.xg.gnl.DataUtils;
import org.xg.pay.rewardPlan.TRewardPlan;

import java.util.Map;

public class OrgAgentOrderStat extends RecursiveTreeObject<OrgAgentOrderStat> {
  private String orgAgentId;
  private String profId;
  private String profName;
  private Integer productId;
  private String prodName;
  private double qty;
  private double actualCost;
  private String creationTimeS;
  private Integer status;
  private Double reward;

  public OrgAgentOrderStat() { }

  public OrgAgentOrderStat(
    String orgAgentId,
    String profId,
    String profName,
    Integer productId,
    String prodName,
    double qty,
    double actualCost,
    String creationTimeS,
    Integer status,
    Double reward
  ) {
    this.orgAgentId = orgAgentId;
    this.profId = profId;
    this.profName = profName;
    this.productId = productId;
    this.prodName = prodName;
    this.qty = qty;
    this.actualCost = DataUtils.roundMoney(actualCost);
    this.creationTimeS = creationTimeS;
    this.status = status;
    this.reward = reward;
  }

  public boolean getNotPayed() {
    return status == OrderStatusLogics.Status_CreatedNotPaid();
  }

  public String getOrgAgentId() {
    return orgAgentId;
  }

  public void setOrgAgentId(String orgAgentId) {
    this.orgAgentId = orgAgentId;
  }

  public String getProfId() {
    return profId;
  }

  public void setProfId(String profId) {
    this.profId = profId;
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

  public String getCreationTimeS() {
    return creationTimeS;
  }

  public void setCreationTimeS(String creationTimeS) {
    this.creationTimeS = creationTimeS;
  }

  public String getProdName() {
    return prodName;
  }

  public String getProfName() {
    return profName;
  }

  public Double getReward() {
    return reward;
  }

  public static OrgAgentOrderStat fromM(
    MOrgAgentOrderStat os,
    Map<Integer, Product> prodMap,
    Map<String, MedProf> profMap,
    TRewardPlan rewardPlan
  ) {
    Product product = prodMap.get(os.productId());
    Double reward = rewardPlan != null ?
      rewardPlan.reward(product.getId(), product.getPrice0()) : 0.0;
    return new OrgAgentOrderStat(
      os.orgAgentId(),
      os.profId(),
      profMap.get(os.profId()).getName(),
      os.productId(),
      product.getName(),
      os.qty(),
      os.actualCost(),
      os.creationTimeS(),
      os.status(),
      reward
    );
  }
}
