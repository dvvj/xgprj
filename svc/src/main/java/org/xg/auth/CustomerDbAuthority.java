package org.xg.auth;

import org.xg.DbConfig;
import org.xg.db.api.TDbOps;
import org.xg.db.impl.DbOpsImpl;
import org.xg.db.impl.Utils;
import scala.Enumeration;

import java.sql.Connection;
import java.util.Map;

public final class CustomerDbAuthority {

  private final TAuthority<String> _auth;

  private CustomerDbAuthority(Map<String, byte[]> userPassMap) {
    _auth = AuthorityBasicImpl.instanceJ(userPassMap);
  }

  public static boolean authenticate(String uid, byte[] passHash) {
    String token = instance._auth.authenticate(uid, passHash);
    return instance._auth.isValidToken(token);
  }

  private static CustomerDbAuthority createInstasnce() {
    try {
      Connection conn = Utils.tryConnect(DbConfig.ConnectionStr);

      System.out.println("connected!");

      TDbOps dbOps = DbOpsImpl.jdbcImpl(conn);
      Map<String, byte[]> userPassMap = dbOps.getUserPassMapJ();
      conn.close();

      return new CustomerDbAuthority(userPassMap);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Error", ex);
    }
  }

  private final static CustomerDbAuthority instance = createInstasnce();

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
