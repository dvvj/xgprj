package org.xg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.xg.ui.model.Product;

public class ProductTableController {
  @FXML
  private TableView<Product> tblProducts;

  @FXML
  public void initialize() {

    //resourcesLabel.setText(resources.getBaseBundleName());
  }
}
