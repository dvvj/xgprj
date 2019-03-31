package org.xg.ui.model;

import org.xg.dbModels.MOrgAgentOrderStat;
import org.xg.ui.utils.Global;

import java.util.HashMap;
import java.util.Map;

public class OrderStatusUIHelpers {

  private final static Map<Integer, String> statusStrMap;
  static {
    statusStrMap = new HashMap<>();
    statusStrMap.put(MOrgAgentOrderStat.Status_CreatedNotPaid(), Global.AllRes.getString("orderTable.status.orderNotPayed"));
    statusStrMap.put(MOrgAgentOrderStat.Status_Paid(), Global.AllRes.getString("orderTable.status.orderPayed"));
    statusStrMap.put(MOrgAgentOrderStat.Status_Locked(), Global.AllRes.getString("orderTable.status.orderLocked"));
  }


}
