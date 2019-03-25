package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.MMedProf;

public class MedProf extends RecursiveTreeObject<MedProf> {
  private String uid;
  private String name;
  private String mobile;
  private String orgId;

  public MedProf() { }

  public MedProf(
    String uid,
    String name,
    String mobile,
    String orgId
  ) {
    this.uid = uid;
    this.name = name;
    this.mobile = mobile;
    this.orgId = orgId;
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

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public static MedProf fromMMedProf(MMedProf mp) {
    return new MedProf(mp.profId(), mp.name(), mp.mobile(), mp.orgId());
  }
}
