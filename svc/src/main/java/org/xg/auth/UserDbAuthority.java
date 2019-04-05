package org.xg.auth;

import org.xg.SvcUtils;
import org.xg.gnl.CachedData;
import org.xg.log.Logging;
import org.xg.svc.AuthResult;

import java.util.Map;
import java.util.logging.Logger;

public final class UserDbAuthority {

  private TAuthority _auth;

  private void update(TAuthority auth) {
    _auth = auth;
  }

  private final static Logger logger = Logger.getLogger(UserDbAuthority.class.getName());

  private UserDbAuthority(Map<String, byte[]> userPassMap) {
    _auth = AuthorityBasicImpl.instanceJ(userPassMap);
  }

//  public static boolean authenticateCustomer(String uid, byte[] passHash) {
//    String token = customerInstance._auth.authenticate(uid, passHash);
//    return customerInstance._auth.isValidToken(token);
//  }

  private static boolean authenticateUser(TAuthority auth, String uid, String hashStr) {
    AuthResult res = auth.authenticate(uid, hashStr);

    if (!res.result()) {
      logger.info(
        String.format("failed to authenticate [%s], reason: %s", uid, res.msg())
      );
    }
    return res.result();
  }

  private static UserDbAuthority createDb(Map<String, byte[]> userPassMap) {
    try {
      return new UserDbAuthority(userPassMap);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }
  }

  private final static CachedData<UserDbAuthority> customerAuthDb = CachedData.createJ(
    () -> createDb(
      SvcUtils.getDbOps().getUserPassMapJ()
    )
  );
  public static boolean authenticateCustomer(String customerId, String passHashStr) {
    return authenticateUser(customerAuthDb.getData()._auth, customerId, passHashStr);
  }
  public static void updateCustomerDb() {
    customerAuthDb.update();
  }

  private final static CachedData<UserDbAuthority> medProfAuthDb = CachedData.createJ(
    () -> createDb(
      SvcUtils.getDbOps().getMedProfPassMapJ()
    )
  );
  public static boolean authenticateMedProfs(String profId, String passHashStr) {
    return authenticateUser(medProfAuthDb.getData()._auth, profId, passHashStr);
  }
  public static void updateMedProfDb() {
    medProfAuthDb.update();
  }

  public static boolean authenticateProfOrgAgent(String orgAgentId, String passHashStr) {
//    AuthResult res = profOrgAgentInstance._auth.authenticate(orgAgentId, passHashStr);
//    if (!res.result()) {
//      logger.info(
//        String.format("failed to authenticate [%s], reason: %s", orgAgentId, res.msg())
//      );
//    }
//    return res.result();
    return authenticateUser(profOrgAgentInstance._auth, orgAgentId, passHashStr);
  }

  public static boolean authenticateProfOrg(String orgId, String passHashStr) {
    return authenticateUser(profOrgInstance._auth, orgId, passHashStr);
  }

  private static UserDbAuthority createInstasnce(Map<String, byte[]> userPassMap) {
    try {
//      Logging.debug("Creating UserDbAuthority: connecting %s", cfg.infoDbConnStr());
//      Connection conn = Utils.tryConnect(cfg.infoDbConnStr());
//      Logging.debug("Connected!");

//      TDbOps dbOps = SvcUtils.getDbOps(); //DbOpsImpl.jdbcImpl(conn);
//      Map<String, byte[]> userPassMap = dbOps.getUserPassMapJ();
//      conn.close();
      Logging.debug("userPassMap Size: %d", userPassMap.size());

      return new UserDbAuthority(userPassMap);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }
  }

  private static class InstanceCreation {
    private UserDbAuthority instance;
    private final Object _instanceLock = new Object();

    private UserDbAuthority getInstance(Map<String, byte[]> userPassMap) {
      // todo: external config
      // GlobalCfg cfg = SvcUtils.getCfg();
      if (instance == null) {
        synchronized (_instanceLock) {
          if (instance == null) {
            instance = createInstasnce(userPassMap);
//            logger.info(
//              String.format("in getInstance, userPassMap size: %d, instance: %s", userPassMap.size(), instance)
//            );
          }
        }
      }
      return instance;
    }

    private UserDbAuthority update(Map<String, byte[]> userPassMap) {
      synchronized (_instanceLock) {
        return createInstasnce(userPassMap);
      }
    }
  }


//  private final static InstanceCreation customerDb = new InstanceCreation();
//  private static UserDbAuthority customerInstance =
//    customerDb.getInstance(
//      SvcUtils.getDbOps().getUserPassMapJ()
//    );
//  public static void updateCustomerDb() {
//    Map<String, byte[]> customerPassMap = SvcUtils.getDbOps().getUserPassMapJ();
//    synchronized (customerDb._instanceLock) {
//      customerInstance = createInstasnce(customerPassMap);
//    }
//    logger.info("in updateCustomerDb, updated profPass map size: " + customerPassMap.size());
//  }

//  private final static InstanceCreation medProfDb = new InstanceCreation();
//  private static UserDbAuthority medProfInstance =
//    medProfDb.getInstance(
//      SvcUtils.getDbOps().getMedProfPassMapJ()
//    );
//  public static void updateMedProfDb() {
//    Map<String, byte[]> profPassMap = SvcUtils.getDbOps().getMedProfPassMapJ();
//    synchronized (medProfDb._instanceLock) {
//      medProfInstance = createInstasnce(profPassMap);
//    }
//    logger.info("in updateMedProfDb, updated profPass map size: " + profPassMap.size());
//  }

  private final static InstanceCreation profOrgAgentDb = new InstanceCreation();
  private static UserDbAuthority profOrgAgentInstance =
    profOrgAgentDb.getInstance(
      SvcUtils.getDbOps().getProfOrgAgentPassMapJ()
    );
  public static void updateProfOrgAgentDb() {
    Map<String, byte[]> orgPassMap = SvcUtils.getDbOps().getProfOrgAgentPassMapJ();
    synchronized (profOrgAgentDb._instanceLock) {
      profOrgAgentInstance = createInstasnce(orgPassMap);
    }
    logger.info("in updateProfOrgAgentDb, updated profPass map size: " + orgPassMap.size());
  }


  private final static InstanceCreation profOrgDb = new InstanceCreation();
  private static UserDbAuthority profOrgInstance =
    profOrgDb.getInstance(
      SvcUtils.getDbOps().getMedProfOrgPassMapJ()
    );
  public static void updateProfOrgDb() {
    Map<String, byte[]> orgPassMap = SvcUtils.getDbOps().getMedProfOrgPassMapJ();
    synchronized (profOrgDb._instanceLock) {
      profOrgInstance = createInstasnce(orgPassMap);
    }
    logger.info("in updateProfOrgAgentDb, updated profPass map size: " + orgPassMap.size());
  }

//  private static final String _InvalidToken = "";
//
//  @Override
//  public boolean isValidToken(String s) {
//    return s != null && !s.equals(_InvalidToken);
//  }
//
//  @Override
//  public String authenticateCustomer(String uid, String pass, Enumeration.Value utype) {
//    return _InvalidToken;
//  }
}
