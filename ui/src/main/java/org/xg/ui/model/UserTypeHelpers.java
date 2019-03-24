package org.xg.ui.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UserTypeHelpers {
  public final static Integer UT_CUSTOMER = 0;
  public final static Integer UT_MEDPROFS = 1;
  public final static Integer UT_PROFORG = 2;
  public final static Integer UT_AGENCY = 3;
  private static String getResName(ResourceBundle resBundle, String t) {
    return resBundle.getString("login.userType." + t);
  }
  public static Map<Integer, ComboOptionData> createMap(ResourceBundle resBundle) {
    Map<Integer, ComboOptionData> res = new LinkedHashMap<>();
    res.put(UT_CUSTOMER, new ComboOptionData(UT_CUSTOMER, getResName(resBundle, "customer")));
    res.put(UT_MEDPROFS, new ComboOptionData(UT_MEDPROFS, getResName(resBundle, "medprofs")));
    res.put(UT_PROFORG, new ComboOptionData(UT_PROFORG, getResName(resBundle, "pharma")));
    res.put(UT_AGENCY, new ComboOptionData(UT_AGENCY, getResName(resBundle, "agency")));
    return res;
  }

}
