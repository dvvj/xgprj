package org.xg.ui.comp;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import org.xg.ui.model.Product;

import java.io.IOException;

public class ProductQtyTableCell extends TableCell<Product, String> {
  @Override
  protected void updateItem(String item, boolean empty) {
    super.updateItem(item, empty);

    if (empty) {
      setText(null);
      setGraphic(null);
    }
    else {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/ui/ProductQtyTableCell.fxml")
      );

      try {
        HBox hbox = loader.load();
        setGraphic(hbox);
      }
      catch (IOException ex) {
        throw new RuntimeException("Error loading ProductQtyTableCell", ex);
      }
    }
  }
}
