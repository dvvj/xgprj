package org.xg.ui.comp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import org.xg.ui.model.Order;
import org.xg.ui.utils.Global;

public class ModifyOrderTableCell extends TableCell<Order, Order> {
  private ObjectProperty<Order> selectedOrder = new SimpleObjectProperty<>();

  private Button btnModifyOrder;

  @Override
  protected void updateItem(Order item, boolean empty) {
    if (empty) {
      setText(null);
      setGraphic(null);
    }
    else {
      selectedOrder.setValue(item);
      btnModifyOrder = new Button();
      btnModifyOrder.setText(
        Global.AllRes.getString(
          item.getCanBeModified() ? "orderTable.action.modify" : "orderTable.action.cannotBeModified"
        )
      );
      btnModifyOrder.setDisable(!item.getCanBeModified());
      setGraphic(btnModifyOrder);
    }
  }
}
