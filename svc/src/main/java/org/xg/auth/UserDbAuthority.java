package org.xg.auth;

import org.xg.SvcUtils;
import org.xg.gnl.GlobalCfg;
import org.xg.log.Logging;

import java.util.Map;

public final class UserDbAuthority {

  private final TAuthority<String> _auth;

  private UserDbAuthority(Map<String, byte[]> userPassMap) {
    _auth = AuthorityBasicImpl.instanceJ(userPassMap);
  }

//  public static boolean authenticateCustomer(String uid, byte[] passHash) {
//    String token = customerInstance._auth.authenticate(uid, passHash);
//    return customerInstance._auth.isValidToken(token);
//  }

  public static boolean authenticateCustomer(String customerId, String passHashStr) {
    String token = customerInstance._auth.authenticate(customerId, passHashStr);
    return customerInstance._auth.isValidToken(token);
  }

  public static boolean authenticateMedProfs(String profId, String passHashStr) {
    String token = medProfInstance._auth.authenticate(profId, passHashStr);
    return medProfInstance._auth.isValidToken(token);
  }

  public static boolean authenticateProfOrgs(String orgId, String passHashStr) {
    String token = profOrgInstance._auth.authenticate(orgId, passHashStr);
    return profOrgInstance._auth.isValidToken(token);
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
          }
        }
      }
      return instance;
    }
  }

  private final static InstanceCreation customerDb = new InstanceCreation();
  private final static UserDbAuthority customerInstance =
    customerDb.getInstance(
      SvcUtils.getDbOps().getUserPassMapJ()
    );

  private final static InstanceCreation medProfDb = new InstanceCreation();
  private final static UserDbAuthority medProfInstance =
    medProfDb.getInstance(
      SvcUtils.getDbOps().getMedProfPassMapJ()
    );
  public static void invalidateMedProfDb() {
    medProfDb.instance = null;
  }

  private final static InstanceCreation profOrgDb = new InstanceCreation();
  private final static UserDbAuthority profOrgInstance =
    profOrgDb.getInstance(
      SvcUtils.getDbOps().getProfOrgPassMapJ()
    );
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
