package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class ProductTableTestHelper {

  public final static ObservableList<Product> testProducts =
    FXCollections.<Product>observableArrayList(
      new Product(1, "prod1", 15.99, "detail 1", Arrays.asList("kw1", "kw2")),
      new Product(2, "prod2", 115.99, "detail 2", Arrays.asList("kw2", "kw3"))
    );
}
