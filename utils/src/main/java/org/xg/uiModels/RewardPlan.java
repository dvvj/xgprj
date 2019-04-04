package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.MRewardPlan;
import org.xg.pay.rewardPlan.TRewardPlan;

public class RewardPlan extends RecursiveTreeObject<RewardPlan> {
  private String planId;
  private String vtag;
  private String info;

  public RewardPlan() { }

  public RewardPlan(
    String planId,
    String vtag,
    String info

  ) {
    this.planId = planId;
    this.vtag = vtag;
    this.info = info;
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

  public static RewardPlan fromM(MRewardPlan p) {
    return new RewardPlan(
      p.id(),
      p.vtag(),
      p.info()
      //p.getPlan()
    );
  }
}
