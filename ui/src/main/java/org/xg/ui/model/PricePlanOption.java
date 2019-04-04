package org.xg.ui.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MPricePlan;
import org.xg.ui.utils.Global;

import java.util.Arrays;

public class PricePlanOption {
  private final String planId;
  private final String desc;
  private final MPricePlan plan;

  PricePlanOption(String planId, String desc, MPricePlan plan) {
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

  private final static String _NoPricePlanId = "c49a2438-fc30-4103-8e83-86f410a31ed4";
  private final static PricePlanOption _NoPricePlan = new PricePlanOption(
    _NoPricePlanId,
    Global.AllRes.getString("addNewCustomer.pricePlanType.noPricePlan"),
    null
  );

  public static ObservableList<PricePlanOption> pricePlanOptionsIncludingNone(MPricePlan[] pricePlans) {
    PricePlanOption[] planOptions = Arrays.stream(pricePlans).map(p -> new PricePlanOption(p.id(), p.info(), p))
      .toArray(PricePlanOption[]::new);

    PricePlanOption[] res = new PricePlanOption[planOptions.length+1];
    res[0] = _NoPricePlan;
    for (int i = 0; i < planOptions.length; i++) {
      res[i+1] = planOptions[i];
    }
    return FXCollections.observableArrayList(res);
  }

  public static boolean isValidPlan(PricePlanOption planOption) {
    return (planOption != null && !planOption.planId.equals(_NoPricePlanId));
  }
}
