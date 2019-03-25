package org.xg.ui.model;

import org.xg.ui.utils.Global;
import org.xg.uiModels.Order;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class OrderFilterHelpers {
  public final static Integer OF_UNPAID = 0;
  public final static Integer OF_THISMONTH = OF_UNPAID+1;
  public final static Integer OF_LASTMONTH = OF_THISMONTH+1;
  public final static Integer OF_ALL = OF_LASTMONTH+1;

  private static String getResName(ResourceBundle resBundle, String t) {
    return resBundle.getString("orderTable.toolbar.filter." + t);
  }

  public final static Map<Integer, ComboOptionData> FilterOptionsMap;
  public final static Map<Integer, Predicate<Order>> OrderFilterMap;
  static {
    FilterOptionsMap = new LinkedHashMap<>(4);
    FilterOptionsMap.put(OF_UNPAID, new ComboOptionData(OF_UNPAID, getResName(Global.AllRes, "unpaid")));
    FilterOptionsMap.put(OF_THISMONTH, new ComboOptionData(OF_THISMONTH, getResName(Global.AllRes, "thismonth")));
    FilterOptionsMap.put(OF_LASTMONTH, new ComboOptionData(OF_LASTMONTH, getResName(Global.AllRes, "lastmonth")));
    FilterOptionsMap.put(OF_ALL, new ComboOptionData(OF_ALL, getResName(Global.AllRes, "all")));

    OrderFilterMap = new HashMap<>(4);
    OrderFilterMap.put(OF_UNPAID, Order::getNotPayed);
    OrderFilterMap.put(OF_THISMONTH, Order::isCreatedThisMonth);
    OrderFilterMap.put(OF_LASTMONTH, Order::isCreatedLastMonth);
    OrderFilterMap.put(OF_ALL, o -> true);
  }


}
