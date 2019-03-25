package org.xg.ui;

import com.jfoenix.controls.*;
import com.jfoenix.utils.JFXHighlighter;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.xg.svc.ImageInfo;
import org.xg.uiModels.Product;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;
import org.xg.ui.utils.UISvcHelpers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

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
  private JFXHighlighter highlighter = new JFXHighlighter();

  private ObservableList<Product> productsCache;
  private ObservableList<Product> activeProducts;

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
        "productTable.srcCountry",
        resBundle,
        100,
        p -> {
          String resKey = Helpers.srcCountryResKey(p.getDetail().getSrcCountry());
          return Global.AllRes.getString(resKey);
        }
      ),
      TableViewHelper.<Product, Double>jfxTableColumnResBundle(
        "productTable.price0",
        resBundle,
        120,
        Product::getPrice0
      ),
      TableViewHelper.<Product, String>jfxTableColumnResBundle(
        "productTable.price",
        resBundle,
        120,
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
//          productsCache = resp;
//          UIHelpers.setRoot4TreeView(tblProducts, productsCache);
//          Global.loggingTodo(String.format("found %d products", productsCache.size()));
//          //tblProducts.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener(productsCache));
//          if (productsCache.size() > 0) {
//            TreeItem<Product> first =  tblProducts.getTreeItem(0);
//            tblProducts.getSelectionModel().select(first);
//            updateSelection(first.getValue());
//          }
          productsCache = resp;
          filterAndUpdateProductTable1(p -> true); // show all

          tblProducts.getSelectionModel().selectedItemProperty().addListener(
            new ItemSelectChangeListener()
          );

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

  private void filterAndUpdateProductTable1(Predicate<Product> filter) {
    if (productsCache != null && productsCache.size() > 0) {
      activeProducts = productsCache.filtered(filter);

      UIHelpers.setRoot4TreeView(tblProducts, activeProducts);
      Global.loggingTodo(String.format("found %d products", activeProducts.size()));
      //tblProducts.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener(productsCache));
      if (activeProducts.size() > 0) {
        TreeItem<Product> first =  tblProducts.getTreeItem(0);
        tblProducts.getSelectionModel().select(first);
        updateSelection(first.getValue());
      }
      else {
        UIHelpers.setPlaceHolder4EmptyTreeView(tblProducts, "productTable.emptyPlaceHolder");
      }
    }
  }
  private void filterAndUpdateProductTable2(Predicate<Product> filter) {
    Task<Integer> task = Helpers.uiTaskJ(
      () -> {
        return 0;
      },
      resp -> {
        filterAndUpdateProductTable1(filter);
        return null;
      },
      1000
    );
    new Thread(task).start();
//    if (productsCache != null && productsCache.size() > 0) {
//      activeProducts = productsCache.filtered(filter);
//
//      UIHelpers.setRoot4TreeView(tblProducts, activeProducts);
//      Global.loggingTodo(String.format("found %d products", activeProducts.size()));
//      //tblProducts.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener(productsCache));
//      if (activeProducts.size() > 0) {
//        TreeItem<Product> first =  tblProducts.getTreeItem(0);
//        tblProducts.getSelectionModel().select(first);
//        updateSelection(first.getValue());
//      }
//    }
  }

  @FXML
  JFXTextField txtSearch;
  @FXML
  JFXButton btnFilter;


  @Override
  public void initialize(URL location, ResourceBundle resBundle) {
    //tblProducts.itemsProperty().bindBidirectional(productsCache);


    //tblProducts.setColumnResizePolicy(TreeTableView.UNCONSTRAINED_RESIZE_POLICY);
    setupAndFetchProductTable(resBundle);

    txtSearch.setOnKeyReleased(e -> highlighter.highlight(tblProducts, txtSearch.getText()));

//    btnFilter.setOnAction(e -> filterAndUpdateProductTable2(
//      p -> {
//        System.out.println(p.getName() + ":" + txtSearch.getText());
//        return p.getName().toLowerCase().contains(txtSearch.getText().toLowerCase());
//      }
//    ));

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

  private class ItemSelectChangeListener<T extends TreeItem<Product>> implements ChangeListener<T> {
//    private final ObservableList<Product> products;
    ItemSelectChangeListener() { }
    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {

      boolean hasUpdate = newValue != null && oldValue != null && !oldValue.getValue().getId().equals(newValue.getValue().getId());

      if (hasUpdate) {
        Product prod = newValue.getValue();
        System.out.println("new selection: " + prod.getName());
        updateSelection(prod);
//        selectedProductDetail.setValue(prod.getDetail().getDesc());
//        ImageInfo imgInfo = new ImageInfo(prod.getId(), prod.getAssets().get(0).url());
//        String url = imgInfo.getUrl(Global.getServerCfg());
//        System.out.println("Getting image: " + url);
////        Image img = new Image(url, true);
////        img.progressProperty().addListener(new ChangeListener<Number>() {
////          @Override
////          public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
////            if (newValue.doubleValue() >= 1.0) {
////              System.out.println("load complete!");
////              selectedProductImg.set(img);
////            }
////          }
////        });
//        selectedProduct.setValue(prod);
//
//        selectedProductImageUrl.setValue(url);
      }

    }
  }

  private void updateSelection(Product prod) {
    selectedProductDetail.setValue(prod.getDetail().getDesc());
    ImageInfo imgInfo = new ImageInfo(prod.getId(), prod.getAssets().get(0).url());
    String url = imgInfo.getUrl(UISvcHelpers.serverCfg());
    selectedProduct.setValue(prod);
    selectedProductImageUrl.setValue(url);
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
        selectedProductDetail.setValue(prod.getDetail().getDesc());
        ImageInfo imgInfo = new ImageInfo(prod.getId(), prod.getAssets().get(0).url());
        String url = imgInfo.getUrl(UISvcHelpers.serverCfg());
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
