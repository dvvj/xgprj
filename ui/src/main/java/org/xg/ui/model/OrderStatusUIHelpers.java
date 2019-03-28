package org.xg.ui.model;

import org.xg.dbModels.MOrder;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.ui.utils.Global;
import org.xg.uiModels.Order;

import java.util.HashMap;
import java.util.Map;
import static org.xg.uiModels.Order.*;

public class OrderStatusUIHelpers {

  private final static Map<Integer, String> statusStrMap;
  static {
    statusStrMap = new HashMap<>();
    statusStrMap.put(MOrgOrderStat.Status_CreatedNotPaid(), Global.AllRes.getString("orderTable.status.orderNotPayed"));
    statusStrMap.put(MOrgOrderStat.Status_Paid(), Global.AllRes.getString("orderTable.status.orderPayed"));
    statusStrMap.put(MOrgOrderStat.Status_Locked(), Global.AllRes.getString("orderTable.status.orderLocked"));
  }


}
