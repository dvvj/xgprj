package org.xg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.xg.auth.UserDbAuthority;
import org.xg.dbModels.*;
import org.xg.gnl.GlobalCfg;
import org.xg.svc.AddNewCustomer;
import org.xg.svc.AddNewMedProf;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SvcUtils {

  private final static ObjectMapper ObjMapper = new ObjectMapper();

  private final static Logger logger = Logger.getLogger(SvcUtils.class.getName());

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

  public static void updateMedProfs() {
    synchronized (_lockMedProfs) {
      _medProfs = null;
    }
    UserDbAuthority.updateMedProfDb();
    logger.info("updateMedProfs called, current _medProfs: " + _medProfs);
  }

  public static void updateCustomers() {
    synchronized (_lockCustomers) {
      _customers = null;
    }
    UserDbAuthority.updateCustomerDb();
    logger.info("updateMedProfs called, current _medProfs: " + _customers);
  }


  public static MMedProf[] getMedProfsOf(String orgId) {
    List<MMedProf> medprofs = getMedProfs().values().stream().filter(mp -> mp.orgId().equals(orgId))
      .collect(Collectors.toList());
    MMedProf[] res = new MMedProf[medprofs.size()];
    return medprofs.toArray(res);
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

  private static Object _lockProfOrgs = new Object();
  private static Map<String, MProfOrg> _profOrgs = null;
  public static Map<String, MProfOrg> getProfOrgMaps() {
    if (_profOrgs == null) {
      synchronized (_lockProfOrgs) {
        if (_profOrgs == null) {
          _profOrgs = getDbOps().profOrgMapJ();
        }
      }
    }
    return _profOrgs;
  }

  public static MProfOrg getMedProfOrg(String profId) {
    MMedProf prof = getMedProfs().get(profId);
    MProfOrg profOrg = getProfOrgMaps().get(prof.orgId());
    return profOrg;
  }

//  private static Object _lockOrderStats = new Object();
//  private static Map<String, MOrgOrderStat[]> _orgOrderStats = null;
  public static MOrgOrderStat[] getOrgOrderStatsOf(String orgId) {
    return getDbOps().getOrderStat4Org(orgId);
  }

  public static void addNewMedProf(AddNewMedProf mp) {
    MMedProf mmp = mp.medProf();
    getDbOps().addNewMedProf(
      mmp.profId(),
      mmp.name(),
      mp.pass(),
      mmp.idCardNo(),
      mmp.mobile(),
      mmp.orgId()
    );

    updateMedProfs();
  }

  public static void addNewCustomer(AddNewCustomer nc) {
    MCustomer mc = nc.customer();
    getDbOps().addNewCustomer(
      mc.uid(),
      mc.name(),
      nc.pass(),
      mc.idCardNo(),
      mc.mobile(),
      mc.postalAddr(),
      mc.refUid(),
      mc.bday()
    );

    updateCustomers();
  }

//
//  private final static GlobalCfg _cfg = loadCfg();
//  public final static GlobalCfg getCfg() {
//    return _cfg;
//  }
}
