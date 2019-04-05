package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MPricePlan;

public class Customer extends RecursiveTreeObject<Customer> {
  private String uid;
  private String name;
  private String mobile;
  private String refUid;
  private MPricePlan pricePlan;

  public String getUid() {
    return uid;
  }

  public Customer(
    String uid,
    String name,
    String mobile,
    String refUid,
    MPricePlan pricePlan
  ) {
    this.uid = uid;
    this.name = name;
    this.mobile = mobile;
    this.refUid = refUid;
    this.pricePlan = pricePlan;
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

  public String getRefUid() {
    return refUid;
  }

  public void setRefUid(String refUid) {
    this.refUid = refUid;
  }

  public static Customer fromMCustomer(MCustomer customer) {
    return new Customer(customer.uid(), customer.name(), customer.mobile(), customer.refUid(), null);
  }
}
