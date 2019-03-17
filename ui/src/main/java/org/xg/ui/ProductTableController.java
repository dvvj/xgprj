package org.xg.ui;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.xg.svc.ImageInfo;
import org.xg.ui.model.CustomerOrder;
import org.xg.ui.model.Product;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.xg.ui.model.TableViewHelper.*;
import static org.xg.ui.utils.Global.getAllProducts;

public class ProductTableController implements Initializable {
  @FXML
  private JFXTreeTableView<Product> tblProducts;

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


  private ObservableList<Product> productsCache;

  private void setupAndFetchProductTable(ResourceBundle resBundle) {
    tblProducts.getColumns().addAll(
//      jfxProdcutTableOpsColumn(
//        resBundle.getString("productTable.action"),
//
//        260
//      ),
//      jfxProdcutTableInfoColumn(
//        "productTable.name",
//        300
//      ),
      TableViewHelper.<Product, String>jfxTableColumnResBundle(
        "productTable.name",
        resBundle,
        300,
        Product::getName
      ),
      TableViewHelper.<Product, String>jfxTableColumnResBundle(
        "productTable.price",
        resBundle,
        150,
        Product::getPriceDetail
      ),
//      tableColumnResBundle("productTable.detailedInfo",
//        resBundle,
//        "detailedInfo",
//        80
//      ),
      TableViewHelper.<Product, List<String>>jfxTableColumnResBundle(
        "productTable.Keywords",
        resBundle,
        200,
        Product::getKeywords
      )
    );

    tblProducts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    UIHelpers.setPlaceHolder4TreeView(tblProducts, "productTable.placeHolder");

    tblProducts.setColumnResizePolicy(TreeTableView.UNCONSTRAINED_RESIZE_POLICY);

    Task<ObservableList<Product>> fetchProductsTask = Helpers.uiTaskJ(
      () -> {
        try {
          Thread.sleep(1000);
          return getAllProducts();
        }
        catch (Exception ex) {
          Global.loggingTodo(
            String.format(
              "Error fetching customer table for [%s]: %s", Global.getCurrUid(), ex.getMessage()
            )
          );
          return null;
        }
      },
      resp -> {
        if (resp != null) {
          productsCache = resp;
          UIHelpers.setRoot4TreeView(tblProducts, productsCache);
          Global.loggingTodo(String.format("found %d products", productsCache.size()));
          tblProducts.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener(productsCache));

          if (productsCache.size() > 0) {
            tblProducts.getSelectionModel().select(0);
          }
        }
        else {
          // todo: show error
        }
        return null;
      },
      30000
    );

    new Thread(fetchProductsTask).start();
  }


  @Override
  public void initialize(URL location, ResourceBundle resBundle) {
    //tblProducts.itemsProperty().bindBidirectional(productsCache);


    //tblProducts.setColumnResizePolicy(TreeTableView.UNCONSTRAINED_RESIZE_POLICY);
    setupAndFetchProductTable(resBundle);


//    tblProducts.onSortProperty().setValue(new EventHandler<SortEvent<TableView<Product>>>() {
//      @Override
//      public void handle(SortEvent<TableView<Product>> event) {
//        System.out.println("sorted, first prod: " + productsCache.getValue().get(0).getName());
//      }
//    });


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
        String url = imgInfo.getUrl(Global.getServerCfg());
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
