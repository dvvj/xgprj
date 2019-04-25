package org.xg.uiModels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MPricePlan;

import java.util.Arrays;

public class PricePlanOption {
  private final String planId;
  private final String desc;
  private final MPricePlan plan;

  public PricePlanOption(String planId, String desc, MPricePlan plan) {
    this.planId = planId;
    this.desc = desc;
    this.plan = plan;
  }

  @Override
  public String toString() {
    return desc;
  }

  public String getPlanId() {
    return planId;
  }

  public String getDesc() {
    return desc;
  }

  public MPricePlan getPlan() {
    return plan;
  }

}
