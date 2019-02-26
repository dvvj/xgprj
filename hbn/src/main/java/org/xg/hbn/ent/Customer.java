package org.xg.hbn.ent;

import javax.persistence.*;

@Entity
@Table(name="customers", uniqueConstraints = {@UniqueConstraint(columnNames = {"idcard_no"})})
public class Customer {
  @Id
  @Column(name="uid", length = 255, nullable = false)
  private String uid;

  @Column(name="name", length = 31, nullable = false)
  private String name;

  @Column(name="pass_hash", length = 64, nullable = false)
  private byte[] passHash;

  @Column(name="idcard_no", length = 31, nullable = false)
  private String idCardNo;

  @Column(name="mobile", length = 31, nullable = false)
  private String mobile;

  @Column(name="postal_addr", length = 255, nullable = false)
  private String postalAddr;

  @Column(name="bday", length = 10, nullable = false)
  private String bday;

  @Column(name="ref_uid", length = 255, nullable = false)
  private String refUid;

  public Customer() { }

  public Customer(
    String uid,
    String name,
    byte[] passHash,
    String idCardNo,
    String mobile,
    String postalAddr,
    String bday,
    String refUid
  ) {
    this.uid = uid;
    this.name = name;
    this.passHash = passHash;
    this.idCardNo = idCardNo;
    this.mobile = mobile;
    this.postalAddr = postalAddr;
    this.bday = bday;
    this.refUid = refUid;
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

  public String getPostalAddr() {
    return postalAddr;
  }

  public void setPostalAddr(String postalAddr) {
    this.postalAddr = postalAddr;
  }

  public String getBday() {
    return bday;
  }

  public void setBday(String bday) {
    this.bday = bday;
  }

  public String getRefUid() {
    return refUid;
  }

  public void setRefUid(String refUid) {
    this.refUid = refUid;
  }

}
