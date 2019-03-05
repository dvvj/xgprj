package org.xg.auth;

import org.xg.SvcUtils;

public class SessionManager {

  private static SessionMgr _mgr; // = new SessionMgr(30);

  private final static Object _lock = new Object();
  private static SessionMgr getMgr() {
    if (_mgr == null) {
      synchronized (_lock) {
        if (_mgr == null) {
          _mgr = new SessionMgr(SvcUtils.getCfg().timeOutMs());
        }
      }
    }
    return _mgr;
  }

  public static void addSession(String uid, String token) {
    getMgr().addSession(uid, token);
  }

  public static boolean checkSession(String token) {
    String uid = findUid(token);
    return getMgr().checkSession(uid, token).success();
  }

  public static String findUid(String token) {
    return getMgr().reverseLookup(token);
  }
}
