package org.xg.ui.comp;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.xg.ui.model.Product;
import org.xg.ui.utils.Global;

import java.io.IOException;

public class ProductPlaceOrderTableCell extends TableCell<Product, String> {
  @Override
  protected void updateItem(String item, boolean empty) {
    super.updateItem(item, empty);

    if (empty) {
      setText(null);
      setGraphic(null);
    }
    else {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/ui/PlaceOrder.fxml"),
        Global.AllRes
      );

      try {
        VBox vbox = loader.load();
        setGraphic(vbox);
      }
      catch (IOException ex) {
        throw new RuntimeException("Error loading ProductPlaceOrderTableCell", ex);
      }
    }
  }
}
