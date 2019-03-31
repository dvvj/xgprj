package org.xg.hbn.ent;

import javax.persistence.*;

@Entity
@Table(name="med_profs", uniqueConstraints = {@UniqueConstraint(columnNames = {"idcard_no"})})
public class MedProf {
  @Id
  @Column(name="prof_id", length = 255, nullable = false)
  private String profId;

  @Column(name="name", length = 31, nullable = false)
  private String name;

  @Column(name="pass_hash", length = 64, nullable = false)
  private byte[] passHash;

  @Column(name="idcard_no", length = 31, nullable = false)
  private String idCardNo;

  @Column(name="mobile", length = 31, nullable = false)
  private String mobile;

  @Column(name="org_agent_id", length = 31, nullable = false)
  private String orgAgentId;

  public MedProf() { }

  public MedProf(
    String profId,
    String name,
    byte[] passHash,
    String idCardNo,
    String mobile,
    String orgAgentId
  ) {
    this.profId = profId;
    this.name = name;
    this.passHash = passHash;
    this.idCardNo = idCardNo;
    this.mobile = mobile;
    this.orgAgentId = orgAgentId;
  }

  public String getProfId() {
    return profId;
  }

  public void setProfId(String profId) {
    this.profId = profId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getPassHash() {
    return passHash;
  }

  public void setPassHash(byte[] passHash) {
    this.passHash = passHash;
  }

  public String getIdCardNo() {
    return idCardNo;
  }

  public void setIdCardNo(String idCardNo) {
    this.idCardNo = idCardNo;
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
}
