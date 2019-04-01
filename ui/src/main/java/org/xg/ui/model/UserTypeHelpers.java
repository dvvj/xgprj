package org.xg.ui.model;

import org.xg.user.UserType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UserTypeHelpers {
  public final static Integer UT_CUSTOMER = UserType.Customer().code();
  public final static Integer UT_MEDPROFS = UserType.MedProf().code();
  public final static Integer UT_PROFORG_AGENT = UserType.MedProfOrgAgent().code();;
  public final static Integer UT_PROFORG = UserType.MedProfOrg().code();;
  public final static Integer UT_AGENCY = UserType.Agency().code();;
  private static String getResName(ResourceBundle resBundle, String t) {
    return resBundle.getString("login.userType." + t);
  }
  public static Map<Integer, ComboOptionData> createMap(ResourceBundle resBundle) {
    Map<Integer, ComboOptionData> res = new LinkedHashMap<>();
    res.put(UT_CUSTOMER, new ComboOptionData(UT_CUSTOMER, getResName(resBundle, "customer")));
    res.put(UT_MEDPROFS, new ComboOptionData(UT_MEDPROFS, getResName(resBundle, "medprofs")));
    res.put(UT_PROFORG_AGENT, new ComboOptionData(UT_PROFORG_AGENT, getResName(resBundle, "proforgAgent")));
    res.put(UT_PROFORG, new ComboOptionData(UT_PROFORG, getResName(resBundle, "proforg")));
    res.put(UT_AGENCY, new ComboOptionData(UT_AGENCY, getResName(resBundle, "agency")));
    return res;
  }

}
