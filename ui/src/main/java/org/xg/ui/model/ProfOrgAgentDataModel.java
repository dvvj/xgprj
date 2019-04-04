package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MOrgAgentOrderStat;
import org.xg.dbModels.MPricePlan;
import org.xg.dbModels.MRewardPlan;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.utils.Helpers;
import org.xg.uiModels.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProfOrgAgentDataModel {

  private ObservableList<MedProf> medProfs;
  private final OrgAgentOrderStat[] rawOrderStats;
  private final ObservableList<OrgAgentOrderStat> orderStats;
  private Map<String, MedProf> profMap;
  private final ObservableList<RewardPlan> ownedRewardPlans;
  private final ObservableList<RewardPlanOption> rewardPlanOptions;
  private final TRewardPlan rewardPlan;
  private final Map<Integer, Product> prodMap;

  public ObservableList<RewardPlan> getOwnedRewardPlans() {
    return ownedRewardPlans;
  }

  public void setMedProfs(MMedProf[] medProfs) {
    MedProf[] profs = Helpers.convMedProfs(medProfs);
    profMap = Arrays.stream(profs).collect(
      Collectors.toMap(
        MedProf::getUid, Function.identity()
      )
    );
    this.medProfs = FXCollections.observableArrayList(profs);
  }

  public ProfOrgAgentDataModel(
    MMedProf[] medProfs,
    MOrgAgentOrderStat[] orderStats,
    MRewardPlan[] rewardPlans,
    Map<Integer, Product> prodMap,
    TRewardPlan rewardPlan
  ) {
    setMedProfs(medProfs);
    RewardPlan[] plans = Arrays.stream(rewardPlans).map(RewardPlan::fromM).toArray(RewardPlan[]::new);
    this.ownedRewardPlans = FXCollections.observableArrayList(plans);
    rewardPlanOptions = RewardPlanOption.pricePlanOptionsIncludingNone(rewardPlans);
    rawOrderStats = Helpers.convOrgAgentOrderStats(orderStats, profMap, rewardPlan);
    this.orderStats = FXCollections.observableArrayList(rawOrderStats);
    this.rewardPlan = rewardPlan;
    this.prodMap = prodMap;
  }

  public ObservableList<MedProf> getMedProfs() {
    return medProfs;
  }

  public ObservableList<OrgAgentOrderStat> getOrderStats() {
    return orderStats;
  }

  public OrgAgentOrderStat[] getRawOrderStats() {
    return rawOrderStats;
  }

  public double calcTotalReward() {
    if (rewardPlan != null)
      return Helpers.calcRewards(rewardPlan, rawOrderStats, prodMap);
    else
      return 0.0;
  }

  public ObservableList<RewardPlanOption> getRewardPlanOptions() {
    return rewardPlanOptions;
  }

  public TRewardPlan getRewardPlan() {
    return rewardPlan;
  }
}
