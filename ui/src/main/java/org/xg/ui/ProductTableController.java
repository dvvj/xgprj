package org.xg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.xg.ui.model.Product;
import static org.xg.ui.model.ProductTableTestHelper.*;

public class ProductTableController {
  @FXML
  private TableView<Product> tblProducts;

  @FXML
  public void initialize() {

    tblProducts.getItems().addAll(testProducts);
    tblProducts.getColumns().addAll(
      tableColumn("Id", "id"),
      tableColumn("Name", "name"),
      tableColumn("Price", "price0"),
      tableColumn("Detailed Info", "detailedInfo"),
      tableColumn("Keywords", "keywords")
    );

    tblProducts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tblProducts.setPlaceholder(new Label("No data/column"));
    //resourcesLabel.setText(resources.getBaseBundleName());
  }
}
