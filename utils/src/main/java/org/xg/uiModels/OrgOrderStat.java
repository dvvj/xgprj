package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.gnl.DataUtils;

import java.util.Map;

public class OrgOrderStat extends RecursiveTreeObject<OrgOrderStat> {
  private String orgId;
  private String profId;
  private String profName;
  private Integer productId;
  private String prodName;
  private double qty;
  private double actualCost;
  private String creationTimeS;
  private Integer status;

  public OrgOrderStat() { }

  public OrgOrderStat(
    String orgId,
    String profId,
    String profName,
    Integer productId,
    String prodName,
    double qty,
    double actualCost,
    String creationTimeS,
    Integer status
  ) {
    this.orgId = orgId;
    this.profId = profId;
    this.profName = profName;
    this.productId = productId;
    this.prodName = prodName;
    this.qty = qty;
    this.actualCost = DataUtils.roundMoney(actualCost);
    this.creationTimeS = creationTimeS;
    this.status = status;
  }

  public boolean getNotPayed() {
    return status == MOrgOrderStat.Status_CreatedNotPaid();
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

  public static OrgOrderStat fromM(
    MOrgOrderStat os,
    Map<Integer, Product> prodMap,
    Map<String, MedProf> profMap
  ) {
    return new OrgOrderStat(
      os.orgId(),
      os.profId(),
      profMap.get(os.profId()).getName(),
      os.productId(),
      prodMap.get(os.productId()).getName(),
      os.qty(),
      os.actualCost(),
      os.creationTimeS(),
      os.status()
    );
  }
}
