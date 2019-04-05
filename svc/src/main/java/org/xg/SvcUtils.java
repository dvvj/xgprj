package org.xg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.xg.auth.UserDbAuthority;
import org.xg.dbModels.*;
import org.xg.gnl.CachedData;
import org.xg.gnl.GlobalCfg;
import org.xg.svc.AddNewCustomer;
import org.xg.svc.AddNewMedProf;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
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
//  private static Object _lockPricePlanMaps = new Object();
//  private static Map<String, MPricePlanMap> _pricePlanMaps = null;
//  private static Object _lockCustomers = new Object();
//  private static Map<String, MCustomer> _customers = null;
//  private static Object _lockMedProfs = new Object();
//  private static Map<String, MMedProf> _medProfs = null;

  // ======================== customer cache
  private static CachedData<Map<String, MCustomer>> customerCache =
    CachedData.createJ(
      () -> getDbOps().allCustomersToMapJ()
    );

  public static Map<String, MCustomer> getCustomers() {
    return customerCache.getData();
  }
  public static void updateCustomers() {
    customerCache.update();
    UserDbAuthority.updateCustomerDb();
  }
  // ========================

  // ======================== med prof cache
  private static CachedData<Map<String, MMedProf>> medProfsCache =
    CachedData.createJ(
      () -> getDbOps().medProfsMapJ()
    );

  public static Map<String, MMedProf> getMedProfs() {
    return medProfsCache.getData();
  }

  public static void updateMedProfs() {
    medProfsCache.update();
    UserDbAuthority.updateMedProfDb();
  }
  // ========================

  public static MMedProf[] getMedProfsOf(String orgAgentId) {
    List<MMedProf> medprofs = getMedProfs().values().stream().filter(mp -> mp.orgAgentId().equals(orgAgentId))
      .collect(Collectors.toList());
    MMedProf[] res = new MMedProf[medprofs.size()];
    return medprofs.toArray(res);
  }


  public static MCustomer[] getCustomersRefedBy(String profId) {
    return getDbOps().customersRefedBy(profId);
  }

  public static MProfOrgAgent[] getProfOrgAgentsOf(String orgId) {
    return getDbOps().profOrg2AgentMapJ().get(orgId);
  }

  public static MOrder[] getRefedCustomerOrders(String profId) {
    MCustomer[] customers = getCustomersRefedBy(profId);
    String[] customerIds = new String[customers.length];
    for (int i = 0; i < customers.length; i++) {
      customerIds[i] = customers[i].uid();
    }
    return getDbOps().ordersOfCustomers(customerIds);
  }


// ======================== prof org agent cache
  private static CachedData<Map<String, MProfOrgAgent>> profOrgAgentsCache =
    CachedData.createJ(
      () -> getDbOps().profOrgAgentMapJ()
    );

  public static Map<String, MProfOrgAgent> getProfOrgAgentMaps() {
    return profOrgAgentsCache.getData();
  }
//    public static void updateMedProfs() {
//    }
  public static MProfOrgAgent getProfOrgAgent(String profId) {
    MMedProf prof = getMedProfs().get(profId);
    MProfOrgAgent agent = getProfOrgAgentMaps().get(prof.orgAgentId());
    return agent;
  }

//  private static Object _lockOrderStats = new Object();
//  private static Map<String, MOrgOrderStat[]> _orgOrderStats = null;
  public static MOrgAgentOrderStat[] getOrgAgentOrderStatsOf(String orgAgentId) {
    return getDbOps().getOrderStat4OrgAgent(orgAgentId);
  }

  public static void addNewMedProf(AddNewMedProf mp) {
    MMedProf mmp = mp.medProf();
    getDbOps().addNewMedProf(
      mmp.profId(),
      mmp.name(),
      mp.pass(),
      mmp.idCardNo(),
      mmp.mobile(),
      mmp.orgAgentId()
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
