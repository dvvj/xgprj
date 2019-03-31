package org.xg.ui.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MOrgAgentOrderStat;
import org.xg.gnl.DataUtils;
import org.xg.ui.utils.Global;
import org.xg.uiModels.Order;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class CustomerDataModel {
  private final ObservableList<Order> orders;
//  private final ObservableList<Order> filteredOrders;
  private final IntegerProperty filterOptionCode = new SimpleIntegerProperty();

  public CustomerDataModel(Order[] orders) {
    Arrays.stream(orders).forEach(o -> o.setStatusStr(statusStrMap.get(o.getStatus())));
    this.orders = FXCollections.observableArrayList(orders);
    filterOptionCode.setValue(OrderFilterHelpers.OF_UNPAID);
//    filteredOrders = FXCollections.observableArrayList(
//      filterMap.get(filterOptionCode).apply(orders)
//    );
  }

  public void setFilterOptionCode(Integer newCode) {
    this.filterOptionCode.setValue(newCode);
  }

  public void bindFilterOption() {

  }

  public static final Map<Integer, Predicate<Order>> filterMap;
  public static final Map<Integer, String> statusStrMap;
  static {
    filterMap = new HashMap<>();
    filterMap.put(
      OrderFilterHelpers.OF_UNPAID,
      Order::getNotPayed
    );
    filterMap.put(
      OrderFilterHelpers.OF_ALL,
      order -> true
    );
    filterMap.put(
      OrderFilterHelpers.OF_THISMONTH,
      order -> {
        ZonedDateTime zdt = DataUtils.utcTimeNow();
        ZonedDateTime zdtCreated = order.getCreationTime();
        return zdtCreated.getYear() == zdt.getYear() && zdtCreated.getMonthValue() == zdt.getMonthValue();
      }
    );
    filterMap.put(
      OrderFilterHelpers.OF_LASTMONTH,
      order -> {
        ZonedDateTime zdt = DataUtils.utcTimeNow();
        ZonedDateTime zdtStart = zdt.minusMonths(1L).plusDays(1L);
        ZonedDateTime zdtCreated = order.getCreationTime();
        return zdtStart.compareTo(zdtCreated) < 0;
      }
    );

    statusStrMap = new HashMap<>();
    statusStrMap.put(
      MOrgAgentOrderStat.Status_CreatedNotPaid(), Global.AllRes.getString("orderTable.status.createdNotPaid")
    );
    statusStrMap.put(
      MOrgAgentOrderStat.Status_Paid(), Global.AllRes.getString("orderTable.status.paid")
    );
    statusStrMap.put(
      MOrgAgentOrderStat.Status_Cancelled(), Global.AllRes.getString("orderTable.status.cancelled")
    );
    statusStrMap.put(
      MOrgAgentOrderStat.Status_Locked(), Global.AllRes.getString("orderTable.status.locked")
    );
  }

  public ObservableList<Order> getOrders() {
    return orders;
  }
}
