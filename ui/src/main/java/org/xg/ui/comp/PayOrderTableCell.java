package org.xg.ui.comp;

import javafx.scene.control.TableCell;
import org.xg.uiModels.Order;

public class PayOrderTableCell extends TableCell<Order, Order> {
//  private ObjectProperty<Order> selectedOrder = new SimpleObjectProperty<>();
//
//  //private Button btnPayOrder;
//
//  @Override
//  protected void updateItem(Order item, boolean empty) {
//    if (empty) {
//      setText(null);
//      setGraphic(null);
//    }
//    else {
//      if (item.getNotPayed()) {
//        Button btnPayOrder;
//        selectedOrder.setValue(item);
//        btnPayOrder = new Button();
//        btnPayOrder.setText(
//          Global.AllRes.getString("orderTable.action.doPayment")
//        );
//        Order order = selectedOrder.getValue();
//        PayOrder postData = new PayOrder(order.getId(), Global.getCurrUid());
//        btnPayOrder.setOnAction(e -> {
//          String resp = SvcHelpers.post(
//            Global.getServerCfg().payOrderURL(),
//            Global.getCurrToken(),
//            PayOrder.toJson(postData)
//          );
//          Global.loggingTodo(resp);
//        });
//        setGraphic(btnPayOrder);
//      }
//      else {
//        Text txtStatus = new Text();
//        txtStatus.setText(
//          Global.AllRes.getString("orderTable.action.noAction")
//        );
//        setGraphic(txtStatus);
//      }
//    }
//  }
}
