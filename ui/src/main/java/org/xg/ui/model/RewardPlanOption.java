package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MPricePlan;
import org.xg.dbModels.MRewardPlan;
import org.xg.ui.utils.Global;

import java.util.Arrays;

public class RewardPlanOption {
  private final String planId;
  private final String desc;
  private final MRewardPlan plan;

  RewardPlanOption(String planId, String desc, MRewardPlan plan) {
    this.planId = planId;
    this.desc = desc;
    this.plan = plan;
  }

  @Override
  public String toString() {
    return desc;
  }

  public String getPlanId() {
    return planId;
  }

  public String getDesc() {
    return desc;
  }

  public MRewardPlan getPlan() {
    return plan;
  }

  private final static String _NoRewardPlanId = "d49a2438-fc30-4103-8e83-86f410a31ed4";
  private final static RewardPlanOption _NoRewardPlan = new RewardPlanOption(
    _NoRewardPlanId,
    Global.AllRes.getString("addMedProf.rewardPlanType.noRewardPlan"),
    null
  );

  public static ObservableList<RewardPlanOption> pricePlanOptionsIncludingNone(MRewardPlan[] rewardPlans) {
    RewardPlanOption[] planOptions = Arrays.stream(rewardPlans).map(p -> new RewardPlanOption(p.id(), p.info(), p))
      .toArray(RewardPlanOption[]::new);

    RewardPlanOption[] res = new RewardPlanOption[planOptions.length+1];
    res[0] = _NoRewardPlan;
    for (int i = 0; i < planOptions.length; i++) {
      res[i+1] = planOptions[i];
    }
    return FXCollections.observableArrayList(res);
  }

  public static boolean isValidPlan(RewardPlanOption planOption) {
    return (planOption != null && !planOption.planId.equals(_NoRewardPlanId));
  }

}
