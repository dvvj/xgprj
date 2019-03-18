package org.xg.ui.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class OrderFilterHelpers {
  public final static Integer OF_UNPAID = 0;
  public final static Integer OF_THISMONTH = OF_UNPAID+1;
  public final static Integer OF_LASTMONTH = OF_THISMONTH+1;
  public final static Integer OF_ALL = OF_LASTMONTH+1;

  private static String getResName(ResourceBundle resBundle, String t) {
    return resBundle.getString("orderTable.toolbar.filter." + t);
  }

  public static Map<Integer, ComboOptionData> createMap(ResourceBundle resBundle) {
    Map<Integer, ComboOptionData> res = new LinkedHashMap<>();
    res.put(OF_UNPAID, new ComboOptionData(OF_UNPAID, getResName(resBundle, "unpaid")));
    res.put(OF_THISMONTH, new ComboOptionData(OF_THISMONTH, getResName(resBundle, "thismonth")));
    res.put(OF_LASTMONTH, new ComboOptionData(OF_LASTMONTH, getResName(resBundle, "lastmonth")));
    res.put(OF_ALL, new ComboOptionData(OF_ALL, getResName(resBundle, "all")));
    return res;
  }
}
