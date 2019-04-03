package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.dbModels.MProfOrgAgent;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.utils.Helpers;
import org.xg.uiModels.OrgOrderStat;
import org.xg.uiModels.Product;
import org.xg.uiModels.ProfOrgAgent;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MedProfOrgDataModel {
  private final ObservableList<ProfOrgAgent> agents;
  private final OrgOrderStat[] rawOrderStat;
  private final Map<String, ProfOrgAgent> agentMap;
  private final Map<Integer, Product> prodMap;
  private final TRewardPlan rewardPlan;

  public MedProfOrgDataModel(
    MProfOrgAgent[] agents,
    MOrgOrderStat[] rawOrderStat,
    Map<Integer, Product> prodMap,
    TRewardPlan rewardPlan
  ) {
    ProfOrgAgent[] agentsTmp = Arrays.stream(agents).map(ProfOrgAgent::fromM).toArray(ProfOrgAgent[]::new);
    this.agents = FXCollections.observableArrayList(agentsTmp);
    agentMap = Arrays.stream(agentsTmp).collect(Collectors.toMap(
      ProfOrgAgent::getAgentId, Function.identity()
    ));
    this.prodMap = prodMap;
    this.rewardPlan = rewardPlan;
    this.rawOrderStat = Helpers.convOrgOrderStats(rawOrderStat, agentMap, rewardPlan);
  }

  public ObservableList<ProfOrgAgent> getAgents() {
    return agents;
  }

  public double calcTotalReward() {
    if (rewardPlan != null)
      return Helpers.calcRewards(rewardPlan, rawOrderStat, prodMap);
    else
      return 0.0;
  }

  public OrgOrderStat[] getRawOrderStat() {
    return rawOrderStat;
  }

  public Map<String, ProfOrgAgent> getAgentMap() {
    return agentMap;
  }

  public Map<Integer, Product> getProdMap() {
    return prodMap;
  }

  public TRewardPlan getRewardPlan() {
    return rewardPlan;
  }
}
