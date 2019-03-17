package org.xg.ui.comp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.xg.ui.model.Product;

public class ProductInfoTableCellCtrl {
  @FXML
  private Label lblName;

  @FXML
  private Label lblDetail;

  private ObjectProperty<Product> selectedProduct = new SimpleObjectProperty<>();
  public void bindSelectedProduct(ObservableValue<Product> product) {
    selectedProduct.bind(product);
    lblName.textProperty().bind(new SimpleStringProperty(selectedProduct.getValue().getName()));
    lblDetail.textProperty().bind(new SimpleStringProperty(selectedProduct.getValue().getDetailedInfo()));
  }

}
