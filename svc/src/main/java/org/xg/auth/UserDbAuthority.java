package org.xg.auth;

import org.xg.SvcUtils;
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

  public static boolean authenticateCustomer(String customerId, String passHashStr) {
    logger.warning("fucku");
    AuthResult res = customerInstance._auth.authenticate(customerId, passHashStr);
    logger.warning("fucku2");
    if (!res.result()) {
      logger.info(
        String.format("failed to authenticate [%s], reason: %s", customerId, res.msg())
      );
    }
    return res.result();
  }

  public static boolean authenticateMedProfs(String profId, String passHashStr) {
    AuthResult res = medProfInstance._auth.authenticate(profId, passHashStr);
    if (!res.result()) {
      logger.info(
        String.format("failed to authenticate [%s], reason: %s", profId, res.msg())
      );
    }
    return res.result();
  }

  public static boolean authenticateProfOrgAgent(String orgAgentId, String passHashStr) {
    AuthResult res = profOrgInstance._auth.authenticate(orgAgentId, passHashStr);
    if (!res.result()) {
      logger.info(
        String.format("failed to authenticate [%s], reason: %s", orgAgentId, res.msg())
      );
    }
    return res.result();
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

  private final static InstanceCreation customerDb = new InstanceCreation();
  private static UserDbAuthority customerInstance =
    customerDb.getInstance(
      SvcUtils.getDbOps().getUserPassMapJ()
    );
  public static void updateCustomerDb() {
    Map<String, byte[]> customerPassMap = SvcUtils.getDbOps().getUserPassMapJ();
    synchronized (customerDb._instanceLock) {
      customerInstance = createInstasnce(customerPassMap);
    }
    logger.info("in updateMedProfDb, updated profPass map size: " + customerPassMap.size());
  }

  private final static InstanceCreation medProfDb = new InstanceCreation();
  private static UserDbAuthority medProfInstance =
    medProfDb.getInstance(
      SvcUtils.getDbOps().getMedProfPassMapJ()
    );
  public static void updateMedProfDb() {
    Map<String, byte[]> profPassMap = SvcUtils.getDbOps().getMedProfPassMapJ();
    synchronized (medProfDb._instanceLock) {
      medProfInstance = createInstasnce(profPassMap);
    }
    logger.info("in updateMedProfDb, updated profPass map size: " + profPassMap.size());
  }

  private final static InstanceCreation profOrgDb = new InstanceCreation();
  private static UserDbAuthority profOrgInstance =
    profOrgDb.getInstance(
      SvcUtils.getDbOps().getProfOrgAgentPassMapJ()
    );
  public static void updateProfOrgfDb() {
    Map<String, byte[]> orgPassMap = SvcUtils.getDbOps().getProfOrgAgentPassMapJ();
    synchronized (profOrgDb._instanceLock) {
      profOrgInstance = createInstasnce(orgPassMap);
    }
    logger.info("in updateMedProfDb, updated profPass map size: " + orgPassMap.size());
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
