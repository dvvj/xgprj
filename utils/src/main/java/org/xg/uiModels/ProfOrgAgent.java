package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.MProfOrgAgent;

public class ProfOrgAgent extends RecursiveTreeObject<ProfOrgAgent> {
  private String agentId;
  private String name;
  private String phone;

  public ProfOrgAgent() { }

  public ProfOrgAgent(
    String agentId,
    String name,
    String phone
  ) {
    this.agentId = agentId;
    this.name = name;
    this.phone = phone;
  }

  public String getAgentId() {
    return agentId;
  }

  public void setAgentId(String agentId) {
    this.agentId = agentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public static ProfOrgAgent fromM(MProfOrgAgent orgAgent) {
    return new ProfOrgAgent(
      orgAgent.orgAgentId(),
      orgAgent.name(),
      orgAgent.phone()
    );
  }
}
