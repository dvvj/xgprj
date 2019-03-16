package org.xg.ui;

import javafx.concurrent.Task;
import org.xg.auth.AuthResp;
import org.xg.auth.SvcHelpers;
import org.xg.gnl.GlobalCfg;
import org.xg.pay.pricePlan.TPricePlan;
import org.xg.ui.model.UserType;
import org.xg.ui.model.UserTypeHelpers;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;

import java.util.HashMap;
import java.util.Map;

public class LoginHelpers {
  interface ILoginAction {
    void run(String userId, String pass, Runnable onSuccess, Runnable onFailure);
  }

  private final static ILoginAction customerLogin = new ILoginAction() {
    @Override
    public void run(String userId, String pass, Runnable onSuccess, Runnable onFailure) {

      Task<AuthResp> authTask = Helpers.uiTaskJ(
        () -> {
          GlobalCfg cfg = Global.getServerCfg();
          String authUrl = cfg.authCustomerURL(); //"https://localhost:8443/webapi/auth/userPass";

          AuthResp resp = SvcHelpers.authReq(authUrl, userId, pass);
          if (resp.success()) {
            //System.out.println(resp.token());
            Global.updateUidToken(userId, resp.token());
          }
          //todo
          TPricePlan pp = SvcHelpers.getPricePlan4UserJ(cfg.pricePlanURL(), userId, resp.token());
          if (pp != null)
            Global.setPricePlan(pp);
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
  };
  //todo
  private final static ILoginAction medProfsLogin = new ILoginAction() {
    @Override
    public void run(String userId, String pass, Runnable onSuccess, Runnable onFailure) {
      String authUrl = Global.getServerCfg().authProfURL(); //"https://localhost:8443/webapi/auth/userPass";

      Task<AuthResp> authTask = Helpers.uiTaskJ(
        () -> {
          AuthResp resp = SvcHelpers.authReq(authUrl, userId, pass);
          if (resp.success()) {
            //System.out.println(resp.token());
            Global.updateUidToken(userId, resp.token());
          }
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
  };


  private static Map<Integer, ILoginAction> createLoginActionMap() {
    Map<Integer, ILoginAction> res = new HashMap<>();
    res.put(UserTypeHelpers.UT_CUSTOMER, customerLogin);
    res.put(UserTypeHelpers.UT_MEDPROFS, medProfsLogin);
    return res;
  }
  private static final Map<Integer, ILoginAction> loginActionMap = createLoginActionMap();

  public static void onLogin(UserType ut, String uid, String pass, Runnable onSuccess, Runnable onFailure) {
    loginActionMap.get(ut.getCode()).run(
      uid, pass, onSuccess, onFailure
    );
  }
}
