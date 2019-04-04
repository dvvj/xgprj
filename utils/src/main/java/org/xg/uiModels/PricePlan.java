package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.MPricePlan;
import org.xg.pay.pricePlan.TPricePlan;

public class PricePlan extends RecursiveTreeObject<PricePlan> {
  private String planId;
  private String vtag;
  private String info;
  private TPricePlan plan;

  public PricePlan() { }

  public PricePlan(
    String planId,
    String vtag,
    String info,
    TPricePlan plan
  ) {
    this.planId = planId;
    this.vtag = vtag;
    this.info = info;
    this.plan = plan;
  }

  public String getPlanId() {
    return planId;
  }

  public void setPlanId(String planId) {
    this.planId = planId;
  }

  public String getVtag() {
    return vtag;
  }

  public void setVtag(String vtag) {
    this.vtag = vtag;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public TPricePlan getPlan() {
    return plan;
  }

  public static PricePlan fromM(MPricePlan p) {
    return new PricePlan(
      p.id(),
      p.vtag(),
      p.info(),
      p.getPlan()
    );
  }
}
