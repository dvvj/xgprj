package org.xg.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.xg.ui.model.Product;

import java.net.URL;
import java.util.ResourceBundle;

import static org.xg.ui.model.ProductTableTestHelper.*;

public class ProductTableController implements Initializable {
  @FXML
  private TableView<Product> tblProducts;

  @Override
  public void initialize(URL location, ResourceBundle resBundle) {
    tblProducts.getItems().addAll(testProducts);

    tblProducts.getColumns().addAll(
      tableColumnResBundle(
        "productTable.id",
        resBundle,
        "id"
      ),
      tableColumnResBundle(
        "productTable.name",
        resBundle,
        "name"
      ),
      tableColumn("Price", "price0"),
      tableColumn("Detailed Info", "detailedInfo"),
      tableColumn("Keywords", "keywords"),
      tableOpsColumn("Operations")
    );

    tblProducts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tblProducts.setPlaceholder(new Label("No data/column"));
  }

//  @FXML
//  public void initialize() {
//
//    tblProducts.getItems().addAll(testProducts);
//    tblProducts.getColumns().addAll(
//      tableColumn("Id", "id"),
//      tableColumn("Name", "name"),
//      tableColumn("Price", "price0"),
//      tableColumn("Detailed Info", "detailedInfo"),
//      tableColumn("Keywords", "keywords"),
//      tableOpsColumn("Operations")
//    );
//
//    tblProducts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//    tblProducts.setPlaceholder(new Label("No data/column"));
//    //resourcesLabel.setText(resources.getBaseBundleName());
//  }
}
