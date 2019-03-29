package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.utils.Helpers;
import org.xg.uiModels.MedProf;
import org.xg.uiModels.OrgOrderStat;
import org.xg.uiModels.Product;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProfOrgsDataModel {

  private final ObservableList<MedProf> medProfs;
  private final OrgOrderStat[] rawOrderStats;
  private final ObservableList<OrgOrderStat> orderStats;
  private final Map<String, MedProf> profMap;
  private final TRewardPlan rewardPlan;
  private final Map<Integer, Product> prodMap;


  public ProfOrgsDataModel(
    MMedProf[] medProfs,
    MOrgOrderStat[] orderStats,
    Map<Integer, Product> prodMap,
    TRewardPlan rewardPlan
  ) {
    MedProf[] profs = Helpers.convMedProfs(medProfs);
    profMap = Arrays.stream(profs).collect(
      Collectors.toMap(
        MedProf::getUid, Function.identity()
      )
    );
    this.medProfs = FXCollections.observableArrayList(profs);
    rawOrderStats = Helpers.convOrgOrderStats(orderStats, profMap);
    this.orderStats = FXCollections.observableArrayList(rawOrderStats);
    this.rewardPlan = rewardPlan;
    this.prodMap = prodMap;
  }

  public ObservableList<MedProf> getMedProfs() {
    return medProfs;
  }

  public ObservableList<OrgOrderStat> getOrderStats() {
    return orderStats;
  }

  public OrgOrderStat[] getRawOrderStats() {
    return rawOrderStats;
  }

  public double calcTotalReward() {
    if (rewardPlan != null)
      return Helpers.calcRewards(rewardPlan, rawOrderStats, prodMap);
    else
      return 0.0;
  }
}
