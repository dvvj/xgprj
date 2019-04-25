package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MPricePlan;
import org.xg.ui.utils.Global;
import org.xg.uiModels.PricePlanOption;

import java.util.Arrays;

public class MedProfWndHelper {
  private final static String _NoPricePlanId = "c49a2438-fc30-4103-8e83-86f410a31ed4";
  private final static org.xg.uiModels.PricePlanOption _NoPricePlan = new PricePlanOption(
    _NoPricePlanId,
    Global.AllRes.getString("addNewCustomer.pricePlanType.noPricePlan"),
    null
  );

  public static ObservableList<org.xg.uiModels.PricePlanOption> pricePlanOptionsIncludingNone(MPricePlan[] pricePlans) {
    org.xg.uiModels.PricePlanOption[] planOptions = Arrays.stream(pricePlans).map(p -> new PricePlanOption(p.id(), p.info(), p))
      .toArray(org.xg.uiModels.PricePlanOption[]::new);

    org.xg.uiModels.PricePlanOption[] res = new PricePlanOption[planOptions.length+1];
    res[0] = _NoPricePlan;
    for (int i = 0; i < planOptions.length; i++) {
      res[i+1] = planOptions[i];
    }
    return FXCollections.observableArrayList(res);
  }

  public static boolean isValidPlan(PricePlanOption planOption) {
    return (planOption != null && !planOption.getPlanId().equals(_NoPricePlanId));
  }
}
