package org.xg.auth;

import org.xg.DbConfig;
import org.xg.db.api.TDbOps;
import org.xg.db.impl.DbOpsImpl;
import org.xg.db.impl.Utils;
import org.xg.log.Logging;
import scala.Enumeration;

import java.sql.Connection;
import java.util.Map;

public final class CustomerDbAuthority {

  private final TAuthority<String> _auth;

  private CustomerDbAuthority(Map<String, byte[]> userPassMap) {
    _auth = AuthorityBasicImpl.instanceJ(userPassMap);
  }

  public static boolean authenticate(String uid, byte[] passHash) {
    String token = getInstance()._auth.authenticate(uid, passHash);
    return getInstance()._auth.isValidToken(token);
  }

  public static boolean authenticate(String uid, String passHashStr) {
    String token = getInstance()._auth.authenticate(uid, passHashStr);
    return getInstance()._auth.isValidToken(token);
  }

  private static CustomerDbAuthority createInstasnce() {
    try {
      Logging.debug("Creating CustomerDbAuthority: connecting %s", DbConfig.ConnectionStr);
      Connection conn = Utils.tryConnect(DbConfig.ConnectionStr);

      Logging.debug("Connected!");

      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      Map<String, byte[]> userPassMap = dbOps.getUserPassMapJ();
      conn.close();
      Logging.debug("userPassMap Size: %d", userPassMap.size());

      return new CustomerDbAuthority(userPassMap);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }
  }

  private static CustomerDbAuthority instance;
  private final static Object _instanceLock = new Object();

  private static CustomerDbAuthority getInstance() {
    if (instance == null) {
      synchronized (_instanceLock) {
        if (instance == null) {
          instance = createInstasnce();
        }
      }
    }
    return instance;
  }

//  private static final String _InvalidToken = "";
//
//  @Override
//  public boolean isValidToken(String s) {
//    return s != null && !s.equals(_InvalidToken);
//  }
//
//  @Override
//  public String authenticate(String uid, String pass, Enumeration.Value utype) {
//    return _InvalidToken;
//  }
}
