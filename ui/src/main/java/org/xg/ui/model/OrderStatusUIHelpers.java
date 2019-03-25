package org.xg.ui.model;

import org.xg.dbModels.MOrder;
import org.xg.ui.utils.Global;
import org.xg.uiModels.Order;

import java.util.HashMap;
import java.util.Map;
import static org.xg.uiModels.Order.*;

public class OrderStatusUIHelpers {

  private final static Map<Integer, String> statusStrMap;
  static {
    statusStrMap = new HashMap<>();
    statusStrMap.put(OrderStatus_NotPayed, Global.AllRes.getString("orderTable.status.orderNotPayed"));
    statusStrMap.put(OrderStatus_Payed, Global.AllRes.getString("orderTable.status.orderPayed"));
    statusStrMap.put(OrderStatus_Locked, Global.AllRes.getString("orderTable.status.orderLocked"));
  }


}
