package org.xg.uiModels;

import org.xg.dbModels.MOrgOrderStat;
import org.xg.pay.rewardPlan.TRewardPlan;

import java.util.Map;

public class OrgOrderStat {
  private OrgAgentOrderStat agentOrderStat;
  private String orgId;
  private String agentName;
  public OrgOrderStat() { }

  public OrgOrderStat(
    OrgAgentOrderStat agentOrderStat,
    String orgId,
    String agentName
  ) {
    this.agentOrderStat = agentOrderStat;
    this.orgId = orgId;
    this.agentName = agentName;
  }

  public OrgAgentOrderStat getAgentOrderStat() {
    return agentOrderStat;
  }

  public void setAgentOrderStat(OrgAgentOrderStat agentOrderStat) {
    this.agentOrderStat = agentOrderStat;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  private final static String ProfNameNA = "N/A";
  public static OrgOrderStat fromM(
    MOrgOrderStat os,
    Map<Integer, UIProduct> prodMap,
    Map<String, ProfOrgAgent> agentMap,
    TRewardPlan rewardPlan
  ) {
    UIProduct product = prodMap.get(os.productId());
    Double reward = rewardPlan != null ?
      rewardPlan.reward(product.getId(), product.getPrice0()) : 0.0;
    OrgAgentOrderStat agentOrderStat = new OrgAgentOrderStat(
      os.orgAgentId(),
      os.profId(),
      ProfNameNA, //profMap.get(os.profId()).getName(),
      os.productId(),
      product.getName(),
      os.qty(),
      os.actualCost(),
      os.creationTimeS(),
      os.status(),
      reward
    );
    return new OrgOrderStat(
      agentOrderStat,
      os.orgId(),
      agentMap.get(os.orgAgentId()).getName()
    );
  }

}
