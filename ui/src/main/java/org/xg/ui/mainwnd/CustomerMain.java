package org.xg.ui.mainwnd;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.ViewController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.xg.ui.*;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
import org.xg.ui.model.CustomerDataModel;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiModels.CustomerOrder;
import org.xg.uiModels.Order;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Supplier;

import static org.xg.ui.model.TableViewHelper.jfxOrderTableOpsColumn;

@ViewController(value = "/ui/CustomerMain.fxml")
public class CustomerMain {
  private CustomerMainRCtrl rightSideController;
  private ExistingOrdersCtrl orderController;
  private ProductTableController productTableController;

  private CustomerDataModel dataModel;

  @FXML
  private VBox leftSide;
  private Node loadLeftSide() throws IOException {

//    VBox leftSide = new VBox();
//    leftSide.setPadding(
//      new Insets(5)
//    );
//    leftSide.setSpacing(5);

//    HBox greetings = new HBox();
//    greetings.setSpacing(10);
//    Text txtWelcome = new Text();
//    txtWelcome.setText(resBundle.getString("greeting.welcome"));
//    Text txtUserInfo = new Text();
//    txtUserInfo.setText(userInfo);
//    txtUserInfo.setStroke(Color.GREEN);
//    greetings.getChildren().addAll(txtWelcome, txtUserInfo);
//    txtUserName.setText(Global.getCurrUid()); // todo use name instead
//    txtUserName.setFill(Color.WHEAT);

    URL path = UiLoginController.class.getResource("/ui/ProductTable.fxml");
    FXMLLoader productLoader = new FXMLLoader(path, Global.AllRes);
    //productLoader.setLocation(path);
    VBox productTableCtrl = productLoader.load();
    productTableController = productLoader.getController();

    leftSide.getChildren().addAll(productTableCtrl);
    return leftSide;

  }
  @FXML
  StackPane ordersTab;
//  private void loadOrdersTab() throws IOException {
//    URL pathOrders = UiLoginController.class.getResource("/ui/ExistingOrders.fxml");
//    FXMLLoader orderLoader = new FXMLLoader(pathOrders, Global.AllRes);
//    VBox orderCtrl = orderLoader.load();
//    ordersTab.getChildren().addAll(orderCtrl);
//
//    orderController = orderLoader.getController();
//
//  }

  private void loadOrdersTable() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;

    //productLoader.setLocation(path);
    table = tableLoader.load();
    TreeTableViewWithFilterCtrl tblCtrl = tableLoader.getController();
    tblCtrl.setup(
//        "customerOrderTable.toolbar.heading",
      "orderTable.toolbar.refresh",
      "orderTable.toolbar.searchPrompt",
      "orderTable.toolbar.filter",
      "orderTable.emptyPlaceHolder",
      c -> {
        Order order = (Order)c;
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          order.getProdName(),
          order.getStatusStr()
        ));
        return strs;
      }
    );

    tblCtrl.setupColumsAndLoadData(
      tbl -> {
        JFXTreeTableView<Order> theTable = (JFXTreeTableView<Order>)tbl;
        theTable.getColumns().addAll(
          TableViewHelper.jfxTableColumnResBundle(
            "orderTable.prodName",
            Global.AllRes,
            300,
            Order::getProdName
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderTable.qty",
            Global.AllRes,
            100,
            Order::getQty
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderTable.creationTime",
            Global.AllRes,
            200,
            Order::getCreationTime
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderTable.status",
            Global.AllRes,
            80,
            Order::getStatusStr
          ),
          jfxOrderTableOpsColumn(
            Global.AllRes.getString("orderTable.action"),
            80
          )

        );

        UIHelpers.setPlaceHolder4TreeView(theTable, "refedCustomerOrderTable.placeHolder");

      },
      () -> dataModel.getOrders()
    );

    ordersTab.getChildren().addAll(table);

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
      productTableController.getSelectedProduct()
    );

    rightSide.getChildren().addAll(r);
    return rightSide;
  }

  private void loadDataModel() {
    Object[] raw = Helpers.paraActions(
      new Supplier[] {
        () -> UISvcHelpers.updateAllOrders(Global.getCurrToken())
      },
      30000
    );

    dataModel = new CustomerDataModel(
      (Order[])raw[0]
    );
  }

  @PostConstruct
  public void launch() {
    try {

      loadDataModel();
      loadLeftSide();
      loadRightSide();
      loadOrdersTable();

    }
    catch (Exception ex) {
      throw new RuntimeException("Error launching main window!", ex);
    }
  }

  public static final class SettingsController {
    @FXML
    private JFXListView<?> toolbarPopupList;

    // close application
    @FXML
    private void submit() {
      if (toolbarPopupList.getSelectionModel().getSelectedIndex() == 1) {
        Platform.exit();
      }
    }
  }
}
