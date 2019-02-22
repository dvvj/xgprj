package org.xg.auth;

public class SessionManager {

  private final static SessionMgr _mgr = new SessionMgr(30);

  public static void addSession(String uid, String token) {
    _mgr.addSession(uid, token);
  }

  public static boolean checkSession(String token) {
    String uid = findUid(token);
    return _mgr.checkSession(uid, token).success();
  }

  public static String findUid(String token) {
    return _mgr.reverseLookup(token);
  }
}
