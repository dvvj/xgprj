package org.xg.ui;

import javafx.concurrent.Task;
import org.xg.auth.AuthResp;
import org.xg.auth.SvcHelpers;
import org.xg.gnl.GlobalCfg;
import org.xg.pay.pricePlan.TPricePlan;
import org.xg.ui.model.ComboOptionData;
import org.xg.ui.model.UserTypeHelpers;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UISvcHelpers;

import java.util.HashMap;
import java.util.Map;

public class LoginHelpers {
  interface ILoginAction {
    void run(String userId, String pass, Runnable onSuccess, Runnable onFailure);
  }

  private abstract static class LoginActionBase implements ILoginAction {

    private final String _authUrl;

    protected LoginActionBase(String authUrl) {
      _authUrl = authUrl;
    }

    protected abstract void extraAction();

    @Override
    public void run(String userId, String pass, Runnable onSuccess, Runnable onFailure) {

      Task<AuthResp> authTask = Helpers.uiTaskJ(
        () -> {
//          GlobalCfg cfg = UISvcHelpers.serverCfg();
//          String authUrl = cfg.authCustomerURL(); //"https://localhost:443/webapi/auth/userPass";

          AuthResp resp = SvcHelpers.authReq(_authUrl, userId, pass);
          if (resp.success()) {
            //System.out.println(resp.token());
            Global.updateUidToken(userId, resp.token());
          }

          extraAction();
          //todo
//          TPricePlan pp = SvcHelpers.getPricePlan4UserJ(cfg.pricePlanURL(), userId, resp.token());
//          if (pp != null)
//            Global.setPricePlan(pp);
          return resp;
        },
        resp -> {
          if (resp != null && resp.success()) {
            onSuccess.run();
          }
          else {
            onFailure.run();
          }
          return null;
        },
        30000
      );

      new Thread(authTask).start();

    }
  }

  private final static ILoginAction customerLogin = new LoginActionBase(
    UISvcHelpers.serverCfg().authCustomerURL()
  ) {
    @Override
    protected void extraAction() {
      TPricePlan pp = SvcHelpers.getPricePlan4UserJ(
        UISvcHelpers.serverCfg().pricePlanURL(),
        Global.getCurrUid(),
        Global.getCurrToken()
      );
      if (pp != null)
        Global.setPricePlan(pp);
    }
  };
//    new ILoginAction() {
//    @Override
//    public void run(String userId, String pass, Runnable onSuccess, Runnable onFailure) {
//
//      Task<AuthResp> authTask = Helpers.uiTaskJ(
//        () -> {
//          GlobalCfg cfg = UISvcHelpers.serverCfg();
//          String authUrl = cfg.authCustomerURL(); //"https://localhost:443/webapi/auth/userPass";
//
//          AuthResp resp = SvcHelpers.authReq(authUrl, userId, pass);
//          if (resp.success()) {
//            //System.out.println(resp.token());
//            Global.updateUidToken(userId, resp.token());
//          }
//          //todo
//          TPricePlan pp = SvcHelpers.getPricePlan4UserJ(cfg.pricePlanURL(), userId, resp.token());
//          if (pp != null)
//            Global.setPricePlan(pp);
//          return resp;
//        },
//        resp -> {
//          if (resp != null && resp.success()) {
//            onSuccess.run();
//          }
//          else {
//            onFailure.run();
//          }
//          return null;
//        },
//        30000
//      );
//
//      new Thread(authTask).start();
//
//    }
//  };
  //todo
  private final static ILoginAction medProfsLogin = new LoginActionBase(
    UISvcHelpers.serverCfg().authProfURL()
  ) {
    @Override
    protected void extraAction() {

    }
  };
//  new ILoginAction() {
//    @Override
//    public void run(String userId, String pass, Runnable onSuccess, Runnable onFailure) {
//      String authUrl = UISvcHelpers.serverCfg().authProfURL(); //"https://localhost:443/webapi/auth/userPass";
//
//      Task<AuthResp> authTask = Helpers.uiTaskJ(
//        () -> {
//          AuthResp resp = SvcHelpers.authReq(authUrl, userId, pass);
//          if (resp.success()) {
//            //System.out.println(resp.token());
//            Global.updateUidToken(userId, resp.token());
//          }
//          return resp;
//        },
//        resp -> {
//          if (resp != null && resp.success()) {
//            onSuccess.run();
//          }
//          else {
//            onFailure.run();
//          }
//          return null;
//        },
//        30000
//      );
//
//      new Thread(authTask).start();
//
//    }
//  };
  private final static ILoginAction profOrgAgentLogin = new LoginActionBase(
    UISvcHelpers.serverCfg().authProfOrgAgentURL()
  ) {
    @Override
    protected void extraAction() {

    }
  };
//  new ILoginAction() {
//    @Override
//    public void run(String userId, String pass, Runnable onSuccess, Runnable onFailure) {
//      String authUrl = UISvcHelpers.serverCfg().authOrgURL(); //"https://localhost:443/webapi/auth/userPass";
//
//      Task<AuthResp> authTask = Helpers.uiTaskJ(
//        () -> {
//          AuthResp resp = SvcHelpers.authReq(authUrl, userId, pass);
//          if (resp.success()) {
//            //System.out.println(resp.token());
//            Global.updateUidToken(userId, resp.token());
//          }
//          return resp;
//        },
//        resp -> {
//          if (resp != null && resp.success()) {
//            onSuccess.run();
//          }
//          else {
//            onFailure.run();
//          }
//          return null;
//        },
//        30000
//      );
//
//      new Thread(authTask).start();
//
//    }
//  };

  private final static ILoginAction profOrgsLogin = new LoginActionBase(
    UISvcHelpers.serverCfg().authProfOrgURL()
  ) {
    @Override
    protected void extraAction() {

    }
  };

  private static Map<Integer, ILoginAction> createLoginActionMap() {
    Map<Integer, ILoginAction> res = new HashMap<>();
    res.put(UserTypeHelpers.UT_CUSTOMER, customerLogin);
    res.put(UserTypeHelpers.UT_MEDPROFS, medProfsLogin);
    res.put(UserTypeHelpers.UT_PROFORG_AGENT, profOrgAgentLogin);
    res.put(UserTypeHelpers.UT_PROFORG, profOrgsLogin);
    return res;
  }
  private static final Map<Integer, ILoginAction> loginActionMap = createLoginActionMap();

  public static void onLogin(ComboOptionData ut, String uid, String pass, Runnable onSuccess, Runnable onFailure) {
    loginActionMap.get(ut.getCode()).run(
      uid, pass, onSuccess, onFailure
    );
  }
}
