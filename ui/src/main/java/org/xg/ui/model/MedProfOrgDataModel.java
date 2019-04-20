package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.busiLogic.RewardPlanLogics;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.dbModels.MProfOrgAgent;
import org.xg.dbModels.MRewardPlan;
import org.xg.dbModels.MRewardPlanMap;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.utils.Helpers;
import org.xg.uiModels.OrgOrderStat;
import org.xg.uiModels.UIProduct;
import org.xg.uiModels.ProfOrgAgent;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MedProfOrgDataModel {
  private final ObservableList<ProfOrgAgent> agents;
  private final OrgOrderStat[] rawOrderStat;
  private final Map<String, ProfOrgAgent> agentMap;
  private final Map<Integer, UIProduct> prodMap;
  private final Map<String, MRewardPlan> rewardPlanNameMap;
  private final Map<String, TRewardPlan> rewardPlans4Agents;

  public MedProfOrgDataModel(
    MProfOrgAgent[] agents,
    MOrgOrderStat[] rawOrderStat,
    Map<Integer, UIProduct> prodMap,
    MRewardPlan[] rewardPlans,
    MRewardPlanMap[] agentRewardPlanMaps
  ) {
    ProfOrgAgent[] agentsTmp = Arrays.stream(agents).map(ProfOrgAgent::fromM).toArray(ProfOrgAgent[]::new);
    this.agents = FXCollections.observableArrayList(agentsTmp);
    agentMap = Arrays.stream(agentsTmp).collect(Collectors.toMap(
      ProfOrgAgent::getAgentId, Function.identity()
    ));
    this.prodMap = prodMap;
    //this.agentRewardPlans = agentRewardPlans;
    rewardPlanNameMap = Arrays.stream(rewardPlans).collect(
      Collectors.toMap(MRewardPlan::id, Function.identity())
    );
    Map<String, MRewardPlanMap> relRpms = Arrays.stream(agentRewardPlanMaps)
      .filter(rpm -> agentMap.keySet().contains(rpm.uid()))
      .collect(
        Collectors.toMap(
          MRewardPlanMap::uid,
          Function.identity()
        )
      );
    rewardPlans4Agents = relRpms.keySet().stream().collect(
      Collectors.toMap(
        Function.identity(),
        uid -> RewardPlanLogics.rewardPlanForJ(
          uid, relRpms, rewardPlanNameMap
        )
      )
    );
    this.rawOrderStat = Helpers.convOrgOrderStats(rawOrderStat, agentMap, rewardPlans4Agents);
  }

  public ObservableList<ProfOrgAgent> getAgents() {
    return agents;
  }

  public double calcTotalReward() {
    return Helpers.calcRewards(rewardPlans4Agents, rawOrderStat, prodMap);
//    if (rewardPlan != null)
//      return Helpers.calcRewards(rewardPlans4Agents, rawOrderStat, prodMap);
//    else
//      return 0.0;
  }

  public OrgOrderStat[] getRawOrderStat() {
    return rawOrderStat;
  }

  public Map<String, ProfOrgAgent> getAgentMap() {
    return agentMap;
  }

  public Map<Integer, UIProduct> getProdMap() {
    return prodMap;
  }

//  public TRewardPlan getRewardPlan() {
//    return rewardPlan;
//  }
}
