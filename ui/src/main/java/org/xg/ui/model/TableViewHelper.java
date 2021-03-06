package org.xg.ui.model;

import com.jfoenix.controls.JFXTreeTableColumn;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.xg.ui.comp.*;
import org.xg.ui.utils.Global;
import org.xg.uiModels.Order;
import org.xg.uiModels.UIProduct;

import java.util.ResourceBundle;
import java.util.function.Function;

public class TableViewHelper {

//  public final static ObservableList<UIProduct> testProducts =
//    FXCollections.<UIProduct>observableArrayList(
//      new UIProduct(1, "prod1", 15.99, "detail 1", Arrays.asList("kw1", "kw2")),
//      new UIProduct(2, "prod2", 115.99, "detail 2", Arrays.asList("kw2", "kw3")),
//      new UIProduct(3, "prod3", 105.99, "detail 3", Arrays.asList("kw2", "kw3")),
//      new UIProduct(4, "prod4", 95.99, "detail 4", Arrays.asList("kw2", "kw3")),
//      new UIProduct(5, "prod5", 85.99, "detail 5", Arrays.asList("kw2", "kw3")),
//      new UIProduct(6, "prod6", 7.99, "detail 6", Arrays.asList("kw4", "kw5"))
//    );

  public static <TO, T> TableColumn<TO, T> tableColumn(String colName, String propName, int prefWidth) {
    TableColumn<TO, T> col = new TableColumn<>(colName);
    col.setCellValueFactory(new PropertyValueFactory<>(propName));
    col.setPrefWidth(prefWidth);
    //col.setResizable(false);
    return col;
  }

  public static <TO, T> TableColumn<TO, T> tableColumnResBundle(
    String colNameKey,
    ResourceBundle resBundle,
    String propName,
    int prefWidth
  ) {
    return tableColumn(
      resBundle.getString(colNameKey),
      propName,
      prefWidth
    );
  }

  public static <TO, T> JFXTreeTableColumn<TO, T> jfxTableColumn(
    String colName,
    int prefWidth,
    Function<TO, T> fProp
    ) {
    JFXTreeTableColumn<TO, T> col = new JFXTreeTableColumn<>(colName);
    col.setCellValueFactory((TreeTableColumn.CellDataFeatures<TO, T> param) -> {
      if (col.validateValue(param) && param.getValue().getValue() != null)
        return new ReadOnlyObjectWrapper<T>(fProp.apply(param.getValue().getValue()));
      else
        return col.getComputedValue(param);
    });
    col.setPrefWidth(prefWidth);
    //col.setResizable(false);
    return col;
  }

  public static <TO, T> JFXTreeTableColumn<TO, T> jfxTableColumnResBundle(
    String colNameKey,
    int prefWidth,
    Function<TO, T> fProp
  ) {
    return jfxTableColumn(
      Global.AllRes.getString(colNameKey),
      prefWidth,
      fProp
    );
  }

  public static TableColumn<UIProduct, UIProduct> tableOpsColumn(String colName, int prefWidth) {
    TableColumn<UIProduct, UIProduct> col = new TableColumn<>(colName);
    col.setPrefWidth(prefWidth);
    col.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue()));
    col.setCellFactory(c -> new ProductPlaceOrderTableCell());
    return col;
  }

//  public static JFXTreeTableColumn<UIProduct, UIProduct> jfxProdcutTableOpsColumn(String colName, int prefWidth) {
//    JFXTreeTableColumn<UIProduct, UIProduct> col = new JFXTreeTableColumn<>(colName);
//    col.setPrefWidth(prefWidth);
//    col.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValue()));
//    col.setCellFactory(c -> new ProductPlaceOrderJFXTableCell());
//    return col;
//  }

  public static JFXTreeTableColumn<UIProduct, UIProduct> jfxProdcutTableInfoColumn(String colName, int prefWidth) {
    JFXTreeTableColumn<UIProduct, UIProduct> col = new JFXTreeTableColumn<>(colName);
    col.setPrefWidth(prefWidth);
    col.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValue()));
    col.setCellFactory(c -> new ProductInfoTableCell());
    return col;
  }

  public static JFXTreeTableColumn<Order, Order> jfxOrderTableOpsColumn(String colName, int prefWidth) {
    JFXTreeTableColumn<Order, Order> col = new JFXTreeTableColumn<>(colName);
    col.setPrefWidth(prefWidth);
    col.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValue()));
    col.setCellFactory(c -> new CustomerOrderOpsJFXTableCell());
    return col;
  }

//  public static TableColumn<Order, Order> orderTableOpsColumn(String colName, int prefWidth) {
//    TableColumn<Order, Order> col = new TableColumn<>(colName);
//    col.setPrefWidth(prefWidth);
//    col.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue()));
//    col.setCellFactory(c -> new PayOrderTableCell());
//    return col;
//  }
}
