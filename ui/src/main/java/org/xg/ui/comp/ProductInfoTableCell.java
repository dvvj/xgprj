package org.xg.ui.comp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.VBox;
import org.xg.uiModels.UIProduct;
import org.xg.ui.utils.Global;

import java.io.IOException;

public class ProductInfoTableCell extends TreeTableCell<UIProduct, UIProduct> {
  private ObjectProperty<UIProduct> selectedProduct = new SimpleObjectProperty<>();
  @Override
  protected void updateItem(UIProduct item, boolean empty) {
    super.updateItem(item, empty);

    //System.out.println("item: " + item);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/ui/comp/ProductInfoTableCell.fxml"),
        Global.AllRes
      );

      try {
        VBox vbox = loader.load();
        ProductInfoTableCellCtrl ctrl = loader.getController();
        selectedProduct.setValue(item);
        ctrl.bindSelectedProduct(selectedProduct);
        setGraphic(vbox);
      }
      catch (IOException ex) {
        throw new RuntimeException("Error loading ProductInfoTableCell", ex);
      }
    }
  }
}
