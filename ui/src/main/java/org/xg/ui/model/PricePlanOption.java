package org.xg.ui.model;


public class PricePlanOption {
  private final String planId;
  private final String desc;

  PricePlanOption(String planId, String desc) {
    this.planId = planId;
    this.desc = desc;
  }

  @Override
  public String toString() {
    return desc;
  }
}
