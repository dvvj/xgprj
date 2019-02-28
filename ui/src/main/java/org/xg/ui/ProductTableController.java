package org.xg.ui;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import org.xg.auth.SvcHelpers;
import org.xg.db.model.MProduct;
import org.xg.gnl.GlobalCfg;
import org.xg.svc.ImageInfo;
import org.xg.ui.model.Product;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;

import java.net.URL;
import java.util.ResourceBundle;

import static org.xg.ui.model.ProductTableTestHelper.*;

public class ProductTableController implements Initializable {
  @FXML
  private TableView<Product> tblProducts;

  private StringProperty selectedProductDetail = new SimpleStringProperty();
  public StringProperty getSelectedProductDetail() {
    return selectedProductDetail;
  }

  private StringProperty selectedProductImageUrl = new SimpleStringProperty();
  public StringProperty getSelectedProductImageUrl() {
    return selectedProductImageUrl;
  }

//  private ObjectProperty<Image> selectedProductImg = new SimpleObjectProperty<>();
//  public ObjectProperty<Image> getSelectedProductImg() {
//    return selectedProductImg;
//  }

  private static ObservableList<Product> updateAllProducts() {
    GlobalCfg cfg = GlobalCfg.localTestCfg();
    String j = SvcHelpers.get(
      cfg.allProductsURL(),
      Global.getCurrToken()
    );
    MProduct[] products = MProduct.fromJsons(j);
    Product[] prods = Helpers.convProducts(products);
    return FXCollections.<Product>observableArrayList(
      prods
    );
  }

  private ObservableList<Product> productsCache;

  @Override
  public void initialize(URL location, ResourceBundle resBundle) {
    productsCache = updateAllProducts();
    tblProducts.getItems().addAll(productsCache);

    tblProducts.getColumns().addAll(
//      tableColumnResBundle(
//        "productTable.id",
//        resBundle,
//        "id"
//      ),
      tableColumnResBundle(
        "productTable.name",
        resBundle,
        "name",
        80
      ),
      tableColumnResBundle(
        "productTable.price",
        resBundle,
        "price0",
        80
      ),
//      tableColumnResBundle("productTable.detailedInfo",
//        resBundle,
//        "detailedInfo",
//        80
//      ),
      tableColumnResBundle("productTable.Keywords",
        resBundle,
        "keywords",
        80
      ),
      tableOpsColumn(
        resBundle.getString("productTable.action"),

        240
      )
    );

    tblProducts.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    tblProducts.setPlaceholder(new Label("No data/column"));

    tblProducts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    tblProducts.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener(productsCache));

    if (productsCache.size() > 0) {
      tblProducts.getSelectionModel().select(0);
    }
  }

  private ObjectProperty<Product> selectedProduct = new SimpleObjectProperty<>();
  public ObservableValue<Product> getSelectedProduct() {
    return selectedProduct;
  }

  private class RowSelectChangeListener implements ChangeListener<Number> {
    private final ObservableList<Product> products;
    RowSelectChangeListener(ObservableList<Product> products) {
      this.products = products;
    }
    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
      int nv = newValue.intValue();

      boolean hasUpdate = oldValue != null && oldValue.intValue() != nv;

      if (nv >= 0 && nv < products.size() && hasUpdate) {
        Product prod = products.get(nv);
        System.out.println("new selection: " + prod.getName());
        selectedProductDetail.setValue(prod.getDetailedInfo());
        ImageInfo imgInfo = new ImageInfo(prod.getId(), prod.getAssets().get(0).url());
        String url = imgInfo.getUrl(GlobalCfg.localTestCfg());
        System.out.println("Getting image: " + url);
//        Image img = new Image(url, true);
//        img.progressProperty().addListener(new ChangeListener<Number>() {
//          @Override
//          public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//            if (newValue.doubleValue() >= 1.0) {
//              System.out.println("load complete!");
//              selectedProductImg.set(img);
//            }
//          }
//        });
        selectedProduct.setValue(prod);

        selectedProductImageUrl.setValue(url);
      }

    }
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
