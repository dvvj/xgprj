package org.xg.ui.comp;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.xg.auth.SvcHelpers;
import org.xg.svc.PayOrder;
import org.xg.uiModels.Order;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.UISvcHelpers;

public class CustomerOrderOpsJFXTableCell extends TreeTableCell<Order, Order> {
  private ObjectProperty<Order> selectedOrder = new SimpleObjectProperty<>();

  //private Button btnPayOrder;

  @Override
  protected void updateItem(Order item, boolean empty) {
    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {
      if (item.getNotPayed()) {
        selectedOrder.setValue(item);
        HBox hb = new HBox();
        hb.setSpacing(5);
        JFXButton btnPayOrder = createButton(
          Global.AllRes.getString("orderTable.action.doPayment"),
          e -> {
            Order order = selectedOrder.getValue();
//            PayOrder postData = new PayOrder(order.getId(), Global.getCurrUid());
//            String resp = SvcHelpers.post(
//              UISvcHelpers.serverCfg().payOrderURL(),
//              Global.getCurrToken(),
//              PayOrder.toJson(postData)
//            );
//            Global.loggingTodo(resp);
            AlipayWindow.launch(
              order.getId(), order.getActualCost(), order.getProdName(), order.getQty()
            );
          }
        );
        JFXButton btnCancelOrder = createButton(
          Global.AllRes.getString("orderTable.action.doCancel"),
          e -> {
            Order order = selectedOrder.getValue();
            System.out.println("cancel pressed");
            String resp = SvcHelpers.post(
              UISvcHelpers.serverCfg().cancelOrderURL(),
              Global.getCurrToken(),
              order.getId().toString()
            );
            Global.loggingTodo(resp);
//            PayOrder postData = new PayOrder(order.getId(), Global.getCurrUid());
//            String resp = SvcHelpers.post(
//              UISvcHelpers.serverCfg().payOrderURL(),
//              Global.getCurrToken(),
//              PayOrder.toJson(postData)
//            );
//            Global.loggingTodo(resp);
          }
        );
        hb.getChildren().addAll(btnPayOrder, btnCancelOrder);

        setGraphic(hb);
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

  private JFXButton createButton(
    String text,
    EventHandler<ActionEvent> onAction
  ) {
    JFXButton btn;
    btn = new JFXButton();
    btn.setText(text);
    btn.getStyleClass().add("button-raised");
    btn.setOnAction(onAction);
    return btn;
  }
}
