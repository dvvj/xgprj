package org.xg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MPricePlan;
import org.xg.dbModels.MPricePlanMap;
import org.xg.dbModels.TDbOps;
import org.xg.gnl.GlobalCfg;

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

//
//  private final static GlobalCfg _cfg = loadCfg();
//  public final static GlobalCfg getCfg() {
//    return _cfg;
//  }
}
