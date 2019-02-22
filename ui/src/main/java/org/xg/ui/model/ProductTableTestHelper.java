package org.xg.ui.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.xg.ui.comp.ProductQtyTableCell;

import java.util.Arrays;
import java.util.ResourceBundle;

public class ProductTableTestHelper {

  public final static ObservableList<Product> testProducts =
    FXCollections.<Product>observableArrayList(
      new Product(1, "prod1", 15.99, "detail 1", Arrays.asList("kw1", "kw2")),
      new Product(2, "prod2", 115.99, "detail 2", Arrays.asList("kw2", "kw3")),
      new Product(3, "prod3", 105.99, "detail 3", Arrays.asList("kw2", "kw3")),
      new Product(4, "prod4", 95.99, "detail 4", Arrays.asList("kw2", "kw3")),
      new Product(5, "prod5", 85.99, "detail 5", Arrays.asList("kw2", "kw3")),
      new Product(6, "prod6", 7.99, "detail 6", Arrays.asList("kw4", "kw5"))
    );

  public static <T> TableColumn<Product, T> tableColumn(String colName, String propName) {
    TableColumn<Product, T> col = new TableColumn<>(colName);
    col.setCellValueFactory(new PropertyValueFactory<>(propName));
    return col;
  }

  public static <T> TableColumn<Product, T> tableColumnResBundle(
    String colNameKey,
    ResourceBundle resBundle,
    String propName
  ) {
    return tableColumn(
      resBundle.getString(colNameKey),
      propName
    );
  }

  public static TableColumn<Product, String> tableOpsColumn(String colName) {
    TableColumn<Product, String> col = new TableColumn<>(colName);
    //col.setCellValueFactory(new PropertyValueFactory<>(propName));
    col.setCellFactory(c -> new ProductQtyTableCell());
    return col;
  }
}
