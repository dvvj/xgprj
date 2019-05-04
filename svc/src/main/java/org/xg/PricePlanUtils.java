package org.xg;

import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MPricePlan;
import org.xg.dbModels.MPricePlanMap;
import org.xg.gnl.CachedData;
import org.xg.svc.CustomerPricePlan;

import java.util.*;

import static org.xg.SvcUtils.getDbOps;

public class PricePlanUtils {

  // ======================== price plan map
  private static CachedData<Map<String, MPricePlanMap>> pricePlanMapCache =
    CachedData.createJ(
      () -> getDbOps().allActivePricePlansJ()
    );
  public static Map<String, MPricePlanMap> getPricePlanMaps() {
    return pricePlanMapCache.getData();
  }

  public static boolean addPricePlanMap(MPricePlanMap ppm) {
    getDbOps().addPricePlanMap(ppm);
    pricePlanMapCache.update();
    return true;
  }
  // ========================

  // ======================== price plan
  private static CachedData<Map<String, MPricePlan>> pricePlanCache =
    CachedData.createJ(
      () -> getDbOps().allPricePlansToMapJ()
    );

  public static Map<String, MPricePlan> getPricePlans() {
    return pricePlanCache.getData();
  }

  public static void updatePricePlans() {
    pricePlanCache.update();
  }

  public static MPricePlan[] getPricePlansAccessibleBy(String creatorId) {
    Map<String, MPricePlan> plans = new HashMap<>(getPricePlans());
    MPricePlan[] res = plans.values().stream().filter(
      p -> p.creator().equals(creatorId) || p.creator().equals(MPricePlan.builtInCreator())
    )
      .toArray(MPricePlan[]::new);
    return res;
  }
  // ========================

  public static CustomerPricePlan[] getPricePlanMap4Agent(String profId) {
    MCustomer[] customers = SvcUtils.getCustomersRefedBy(profId);
    Set<String> customerIds = new HashSet<>(customers.length);
    for (int i = 0; i < customers.length; i++) {
      customerIds.add(customers[i].uid());
    }
    Map<String, MPricePlanMap> pricePlanMaps = getPricePlanMaps();
    Map<String, MPricePlanMap> pricePlanMapOfCustomers = new HashMap<>(customerIds.size());
    for (String k : pricePlanMaps.keySet()) {
      if (customerIds.contains(k))
        pricePlanMapOfCustomers.put(k, pricePlanMaps.get(k));
    }

    Map<String, MPricePlan> pricePlans = getPricePlans();
    List<CustomerPricePlan> t = new ArrayList<>(customers.length);
    for (String uid : pricePlanMapOfCustomers.keySet()) {
      MPricePlanMap ppm = pricePlanMapOfCustomers.get(uid);
      if (ppm.getPlanIds().length > 1)
        throw new UnsupportedOperationException(
          String.format("multiple plan not supported uid(%s), planIds(%s)", uid, ppm.planIdStr())
        );
      if (pricePlans.containsKey(ppm.planIdStr())) {
        t.add(new CustomerPricePlan(uid, pricePlans.get(ppm.planIdStr())));
      }
      else {
        throw new IllegalArgumentException(
          String.format("Price plan with id (%s) not found", ppm.planIdStr())
        );
      }
    }
    CustomerPricePlan[] res = new CustomerPricePlan[t.size()];
    t.toArray(res);
    return res;
  }

}
