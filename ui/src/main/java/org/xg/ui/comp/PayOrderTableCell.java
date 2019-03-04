package org.xg.ui.comp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.text.Text;
import org.xg.auth.SvcHelpers;
import org.xg.gnl.GlobalCfg;
import org.xg.svc.PayOrder;
import org.xg.ui.model.Order;
import org.xg.ui.utils.Global;

public class PayOrderTableCell extends TableCell<Order, Order> {
  private ObjectProperty<Order> selectedOrder = new SimpleObjectProperty<>();

  //private Button btnPayOrder;

  @Override
  protected void updateItem(Order item, boolean empty) {
    if (empty) {
      setText(null);
      setGraphic(null);
    }
    else {
      if (item.getNotPayed()) {
        Button btnPayOrder;
        selectedOrder.setValue(item);
        btnPayOrder = new Button();
        btnPayOrder.setText(
          Global.AllRes.getString("orderTable.action.doPayment")
        );
        Order order = selectedOrder.getValue();
        PayOrder postData = new PayOrder(order.getId(), Global.getCurrUid());
        btnPayOrder.setOnAction(e -> {
          String resp = SvcHelpers.post(
            GlobalCfg.localTestCfg().payOrderURL(),
            Global.getCurrToken(),
            PayOrder.toJson(postData)
          );
          Global.loggingTodo(resp);
        });
        setGraphic(btnPayOrder);
      }
      else {
        Text txtStatus = new Text();
        txtStatus.setText(
          Global.AllRes.getString("orderTable.action.noAction")
        );
        setGraphic(txtStatus);
      }
    }
  }
}
