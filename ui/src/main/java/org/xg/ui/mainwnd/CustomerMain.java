package org.xg.ui.mainwnd;

import com.jfoenix.controls.JFXComboBox;
import io.datafx.controller.ViewController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.xg.ui.*;
import org.xg.ui.comp.ProductTableController2;
import org.xg.ui.comp.TreeTableViewHelper;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
import org.xg.ui.comp.UpdatePasswordCtrl;
import org.xg.ui.model.ComboOptionData;
import org.xg.ui.model.CustomerWndHelper;
import org.xg.ui.model.OrderFilterHelpers;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiDataModels.DataLoaders;
import org.xg.uiDataModels.TDMCustomer;
import org.xg.uiModels.CustomerProduct;
import org.xg.uiModels.Order;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

import static org.xg.ui.model.TableViewHelper.jfxOrderTableOpsColumn;

@ViewController(value = "/ui/CustomerMain.fxml")
public class CustomerMain {
  private CustomerMainRCtrl rightSideController;
  private ExistingOrdersCtrl orderController;
  //private ProductTableController productTableController;
  private ProductTableController2 productTableController;
  //private CustomerWndHelper dataModel;
  private TDMCustomer dataModel;

  @FXML
  private VBox leftSide;
//  private Node loadLeftSide() throws IOException {
//
//    URL path = UiLoginController.class.getResource("/ui/ProductTable.fxml");
//    FXMLLoader productLoader = new FXMLLoader(path, Global.AllRes);
//    //productLoader.setLocation(path);
//    VBox productTableCtrl = productLoader.load();
//    productTableController = productLoader.getController();
//
//    leftSide.getChildren().addAll(productTableCtrl);
//    return leftSide;
//
//  }

  private Node loadLeftSide() throws Exception {

//    URL path = UiLoginController.class.getResource("/ui/ProductTable.fxml");
//    FXMLLoader productLoader = new FXMLLoader(path, Global.AllRes);
//    //productLoader.setLocation(path);
//    VBox productTable = productLoader.load();
    TreeTableViewWithFilterCtrl<CustomerProduct> treeTableCtrl = TreeTableViewHelper.loadTableToTab(
      leftSide,
      product -> {
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          product.getProduct().getName()
        ));
        return strs;
      },
      () -> dataModel.getProducts(),
      () -> {
        System.out.println("todo");
      },
      "productTable",
      "productTable.placeHolder",
      newProd -> { },
      Arrays.asList(
        TableViewHelper.jfxTableColumnResBundle(
          "productTable.name",
          300,
          cp -> cp.getProduct().getName()
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "productTable.referringProfName",
          100,
          cp -> cp.getRefProf().getName()
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "productTable.srcCountry",
          100,
          cp -> {
            String resKey = Helpers.srcCountryResKey(cp.getProduct().getDetail().getSrcCountry());
            return Global.AllRes.getString(resKey);
          }
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "productTable.price0",
          120,
          cp -> cp.getProduct().getPrice0()
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "productTable.price",
          120,
          cp -> cp.getPriceDetail()
        ),
//      tableColumnResBundle("productTable.detailedInfo",
//        resBundle,
//        "detailedInfo",
//        80
//      ),
        TableViewHelper.jfxTableColumnResBundle(
          "productTable.Keywords",
          200,
          cp -> cp.getProduct().getKeywords()
        )
      )
    );

    productTableController = new ProductTableController2(treeTableCtrl);
    //leftSide.getChildren().addAll(productTable);
    return leftSide;

  }

  @FXML
  VBox ordersTab;

  private TreeTableViewWithFilterCtrl<Order> orderTableCtrl;

  private void loadOrdersTab(Integer filterCode) throws Exception {
    ordersTab.getChildren().clear();
    orderTableCtrl = TreeTableViewHelper.loadTableToTab(
      ordersTab,
      order -> {
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          order.getProdName(),
          order.getStatusStr()
        ));
        return strs;
      },
      () -> dataModel.getOrders(),
      this::reloadOrdersTab,
      "orderTable",
      "orderTable.placeHolder",
      newOrder -> {
      },
      Arrays.asList(
        TableViewHelper.jfxTableColumnResBundle(
          "orderTable.prodName",
          300,
          Order::getProdName
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "orderTable.qty",
          100,
          Order::getQty
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "orderTable.creationTime",
          200,
          Order::getCreationTime
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "orderTable.status",
          80,
          Order::getStatusStr
        ),
        jfxOrderTableOpsColumn(
          Global.AllRes.getString("orderTable.action"),
          180
        )
      )
    );

    orderTableCtrl.addExtraComponents(createOrderFilterCombo(filterCode));
  }

  private ObjectProperty<ComboOptionData> currComboSelection = new SimpleObjectProperty<>();

  private Node createOrderFilterCombo(Integer filterCode) {
    //Integer filterCode = OrderFilterHelpers.OF_UNPAID;
    JFXComboBox<ComboOptionData> cmboFilter = new JFXComboBox<>();
    cmboFilter.getItems().addAll(OrderFilterHelpers.FilterOptionsMap.values());
//    dataModel.bindFilterOption();
    cmboFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
      boolean hasUpdate = newValue != null &&
        (oldValue == null || !oldValue.getCode().equals(newValue.getCode()));

      if (hasUpdate) {
        int newVal = newValue.getCode();
        System.out.println("New selection: " + newVal);
        //dataModel.setFilterOptionCode(newVal);
        orderTableCtrl.filterExisting(
          CustomerWndHelper.filterMap.get(newVal)
        );
        //refreshOrderList(newVal);
      }
    });
    cmboFilter.setValue(OrderFilterHelpers.FilterOptionsMap.get(filterCode));
    currComboSelection.bind(cmboFilter.getSelectionModel().selectedItemProperty());

    return cmboFilter;
  }

  private void reloadOrdersTab() {
    ComboOptionData filterOption = currComboSelection.getValue();
    System.out.println("filterCode: " + filterOption.getCode());
    //currComboSelection.unbind();
    try {
      orderTableCtrl.updateDataAndFilter(
        () -> {
          loadDataModel();
          return dataModel.getOrders();
        },
        CustomerWndHelper.filterMap.get(filterOption.getCode())
      );
    }
    catch (Exception ex) {
      Global.loggingTodo("error re-loading orders tab");
      throw new RuntimeException(ex);
    }
  }

  @FXML
  VBox rightSide;
  private Node loadRightSide() throws IOException {

    FXMLLoader rightSideLoader = new FXMLLoader(
      UiLoginController.class.getResource("/ui/CustomerMainR.fxml"),
      Global.AllRes
    );
    VBox r = rightSideLoader.load();
//    rightSide.setPadding(
//      new Insets(20)
//    );
    rightSideController = rightSideLoader.getController();
    rightSideController.setBinding(
      productTableController.getSelectedProductDetail(),
      productTableController.getSelectedProductImageUrl(),
      productTableController.getSelectedProduct(),
      this::reloadOrdersTab
    );

    rightSide.getChildren().addAll(r);
    return rightSide;
  }

  private void loadDataModel() {
//    Object[] raw = Helpers.paraActions(
//      new Supplier[] {
//        () -> UISvcHelpers.updateAllOrders(Global.getCurrToken())
//      },
//      20000
//    );

//    dataModel = new CustomerWndHelper(
//      (Order[])raw[0]
//    );
    dataModel = DataLoaders.customerDataLoaderJ(
      UISvcHelpers.serverCfg(),
      Global.getCurrToken(),
      CustomerWndHelper.statusStrMap
    ).loadAndConstruct(20000);
  }

  @PostConstruct
  public void launch() {
    try {

      loadDataModel();
      loadLeftSide();
      loadRightSide();
      //loadOrdersTable();
      loadOrdersTab(OrderFilterHelpers.OF_UNPAID);
      loadUpdatePasswordTab();

    }
    catch (Exception ex) {
      throw new RuntimeException("Error launching main window!", ex);
    }
  }

  @FXML
  VBox updatePasswordTab;
  private void loadUpdatePasswordTab() throws Exception {
    UpdatePasswordCtrl.load2Tab(updatePasswordTab);
  }

//  public static final class SettingsController {
//    @FXML
//    private JFXListView<?> toolbarPopupList;
//
//    // close application
//    @FXML
//    private void submit() {
//      if (toolbarPopupList.getSelectionModel().getSelectedIndex() == 1) {
//        Platform.exit();
//      }
//    }
//  }
}
