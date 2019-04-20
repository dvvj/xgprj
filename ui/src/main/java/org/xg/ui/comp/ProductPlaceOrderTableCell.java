package org.xg.ui.comp;

import javafx.scene.control.TableCell;
import org.xg.uiModels.UIProduct;

public class ProductPlaceOrderTableCell extends TableCell<UIProduct, UIProduct> {
//  private ObjectProperty<UIProduct> selectedProduct = new SimpleObjectProperty<>();
//  @Override
//  protected void updateItem(UIProduct item, boolean empty) {
//    super.updateItem(item, empty);
//
//    //System.out.println("item: " + item);
//
//    if (empty) {
//      setText(null);
//      setGraphic(null);
//    }
//    else {
//      FXMLLoader loader = new FXMLLoader(
//        getClass().getResource("/ui/PlaceOrder.fxml"),
//        Global.AllRes
//      );
//
//      try {
//        VBox vbox = loader.load();
//        PlaceOrderCtrl ctrl = loader.getController();
//        selectedProduct.setValue(item);
//        ctrl.bindSelectedProduct(selectedProduct);
//        setGraphic(vbox);
//      }
//      catch (IOException ex) {
//        throw new RuntimeException("Error loading ProductPlaceOrderTableCell", ex);
//      }
//    }
//  }
}
