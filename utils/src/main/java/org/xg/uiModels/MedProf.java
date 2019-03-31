package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.MMedProf;

public class MedProf extends RecursiveTreeObject<MedProf> {
  private String uid;
  private String name;
  private String mobile;
  private String orgAgentId;

  public MedProf() { }

  public MedProf(
    String uid,
    String name,
    String mobile,
    String orgAgentId
  ) {
    this.uid = uid;
    this.name = name;
    this.mobile = mobile;
    this.orgAgentId = orgAgentId;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getOrgAgentId() {
    return orgAgentId;
  }

  public void setOrgAgentId(String orgAgentId) {
    this.orgAgentId = orgAgentId;
  }

  public static MedProf fromMMedProf(MMedProf mp) {
    return new MedProf(mp.profId(), mp.name(), mp.mobile(), mp.orgAgentId());
  }
}
