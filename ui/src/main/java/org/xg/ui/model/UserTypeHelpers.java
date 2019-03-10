package org.xg.ui.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UserTypeHelpers {
  public final static Integer UT_CUSTOMER = 0;
  public final static Integer UT_MEDPROFS = 1;
  public final static Integer UT_PHARMA = 2;
  public final static Integer UT_AGENCY = 3;
  private static String getResName(ResourceBundle resBundle, String t) {
    return resBundle.getString("login.userType." + t);
  }
  public static Map<Integer, UserType> createMap(ResourceBundle resBundle) {
    Map<Integer, UserType> res = new LinkedHashMap<>();
    res.put(UT_CUSTOMER, new UserType(UT_CUSTOMER, getResName(resBundle, "customer")));
    res.put(UT_MEDPROFS, new UserType(UT_MEDPROFS, getResName(resBundle, "medprofs")));
    res.put(UT_PHARMA, new UserType(UT_PHARMA, getResName(resBundle, "pharma")));
    res.put(UT_AGENCY, new UserType(UT_AGENCY, getResName(resBundle, "agency")));
    return res;
  }

}
