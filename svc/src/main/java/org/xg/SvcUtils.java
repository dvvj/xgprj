package org.xg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.xg.dbModels.*;
import org.xg.gnl.GlobalCfg;
import org.xg.hbn.ent.Order;

import javax.ws.rs.core.MediaType;
import java.util.Map;

public class SvcUtils {

  private final static ObjectMapper ObjMapper = new ObjectMapper();

  public static <T> T readObj(String json, Class<T> clz) {
    try {
      return ObjMapper.readValue(json, clz);
    }
    catch (Exception ex) {
      throw new RuntimeException("Error deserializing json", ex);
    }

  }

  final static String MediaType_TXT_UTF8 = MediaType.TEXT_PLAIN + ";charset=utf-8";
  final static String MediaType_JSON_UTF8 = MediaType.APPLICATION_JSON + ";charset=utf-8";

  public static GlobalCfg getCfg() {
    return SvcContextListener.getCfg();
  }

  public static TDbOps getDbOps() {
    return SvcContextListener.getDbOps();
  }

  private static Object _lockPricePlans = new Object();
  private static Map<String, MPricePlan> _pricePlans = null;
  private static Object _lockPricePlanMaps = new Object();
  private static Map<String, MPricePlanMap> _pricePlanMaps = null;
  private static Object _lockCustomers = new Object();
  private static Map<String, MCustomer> _customers = null;
  private static Object _lockMedProfs = new Object();
  private static Map<String, MMedProf> _medProfs = null;

  public static Map<String, MCustomer> getCustomers() {
    if (_customers == null) {
      synchronized (_lockCustomers) {
        if (_customers == null) {
          _customers = getDbOps().allCustomersToMapJ();
        }
      }
    }
    return _customers;
  }

  public static Map<String, MMedProf> getMedProfs() {
    if (_medProfs == null) {
      synchronized (_lockMedProfs) {
        if (_medProfs == null) {
          _medProfs = getDbOps().medProfsMapJ();
        }
      }
    }
    return _medProfs;
  }

  public static Map<String, MPricePlanMap> getPricePlanMaps() {
    if (_pricePlanMaps == null) {
      synchronized (_lockPricePlanMaps) {
        if (_pricePlanMaps == null) {
          _pricePlanMaps = getDbOps().allActivePricePlansJ();
        }
      }
    }
    return _pricePlanMaps;
  }


  public static Map<String, MPricePlan> getPricePlans() {
    if (_pricePlans == null) {
      synchronized (_lockPricePlans) {
        if (_pricePlans == null) {
          _pricePlans = getDbOps().allPricePlansToMapJ();
        }
      }
    }
    return _pricePlans;
  }

  private static Object _lockRewardPlans = new Object();
  private static Map<String, MRewardPlan> _rewardPlans = null;
  private static Object _lockRewardPlanMaps = new Object();
  private static Map<String, MRewardPlanMap> _rewardPlanMaps = null;

  public static Map<String, MRewardPlan> getRewardPlans() {
    if (_rewardPlans == null) {
      synchronized (_lockRewardPlans) {
        if (_rewardPlans == null) {
          _rewardPlans = getDbOps().allRewardPlansToMapJ();
        }
      }
    }
    return _rewardPlans;
  }

  public static Map<String, MRewardPlanMap> getRewardPlanMaps() {
    if (_rewardPlanMaps == null) {
      synchronized (_lockRewardPlanMaps) {
        if (_rewardPlanMaps == null) {
          _rewardPlanMaps = getDbOps().allActiveRewardPlansJ();
        }
      }
    }
    return _rewardPlanMaps;
  }

  public static MCustomer[] getCustomersRefedBy(String profId) {
    return getDbOps().customersRefedBy(profId);
  }

  public static MOrder[] getRefedCustomerOrders(String profId) {
    MCustomer[] customers = getCustomersRefedBy(profId);
    String[] customerIds = new String[customers.length];
    for (int i = 0; i < customers.length; i++) {
      customerIds[i] = customers[i].uid();
    }
    return getDbOps().ordersOfCustomers(customerIds);
  }

//
//  private final static GlobalCfg _cfg = loadCfg();
//  public final static GlobalCfg getCfg() {
//    return _cfg;
//  }
}
