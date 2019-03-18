package org.xg.hbn.ent;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="prof_orgs", uniqueConstraints = {@UniqueConstraint(columnNames = {"org_no"})})
public class ProfOrg {
  @Id
  @Column(name="org_id", length = 255, nullable = false)
  private String orgId;

  @Column(name="name", length = 31, nullable = false)
  private String name;

  @Column(name="pass_hash", length = 64, nullable = false)
  private byte[] passHash;

  @Column(name="info", length = 8191, nullable = false)
  private String info;

  @Column(name="org_no", length = 255, nullable = false)
  private String orgNo;

  @Column(name="phone", length = 31, nullable = false)
  private String phone;

  @Column(name="join_date", nullable = false)
  private ZonedDateTime joinDate;

  public ProfOrg() { }

  public ProfOrg(
    String orgId,
    String name,
    byte[] passHash,
    String info,
    String orgNo,
    String phone,
    ZonedDateTime joinDate
  ) {
    this.orgId = orgId;
    this.name = name;
    this.passHash = passHash;
    this.info = info;
    this.orgNo = orgNo;
    this.phone = phone;
    this.joinDate = joinDate;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
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

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getOrgNo() {
    return orgNo;
  }

  public void setOrgNo(String orgNo) {
    this.orgNo = orgNo;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public ZonedDateTime getJoinDate() {
    return joinDate;
  }

  public void setJoinDate(ZonedDateTime joinDate) {
    this.joinDate = joinDate;
  }
}
