package org.xg.ui.comp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.xg.ui.model.Product;
import org.xg.ui.utils.Global;

import java.io.IOException;

public class ProductPlaceOrderTableCell extends TableCell<Product, Product> {
  private ObjectProperty<Product> selectedProduct = new SimpleObjectProperty<>();
  @Override
  protected void updateItem(Product item, boolean empty) {
    super.updateItem(item, empty);

    System.out.println("item: " + item);

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
        PlaceOrderCtrl ctrl = loader.getController();
        selectedProduct.setValue(item);
        ctrl.bindSelectedProduct(selectedProduct);
        setGraphic(vbox);
      }
      catch (IOException ex) {
        throw new RuntimeException("Error loading ProductPlaceOrderTableCell", ex);
      }
    }
  }
}
