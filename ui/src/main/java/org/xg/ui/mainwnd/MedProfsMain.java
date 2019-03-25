package org.xg.ui.mainwnd;

import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.xg.chart.ChartHelpers;
import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MOrder;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.UiLoginController;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
import org.xg.uiModels.Customer;
import org.xg.uiModels.CustomerOrder;
import org.xg.ui.model.MedProfsDataModel;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;
import org.xg.ui.utils.UISvcHelpers;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@ViewController(value = "/ui/MedProfsMain.fxml")
public class MedProfsMain {

  @FXML
  private StackPane ordersTab;

  @FXML
  private HBox customersTab;

  private MedProfsDataModel dataModel = null;

  private void loadDataModel() {
    String profId = Global.getCurrUid();
    String token = Global.getCurrToken();
    Object[] raw = Helpers.paraActions(
      new Supplier[] {
        () -> UISvcHelpers.updateAllRefedCustomers(profId, token),
        () -> UISvcHelpers.updateAllOrdersOfRefedCustomers(profId, token),
        () -> UISvcHelpers.updateRewardPlans(profId, token)
      },
      30000
    );

    dataModel = new MedProfsDataModel(
      (MCustomer[])raw[0],
      (MOrder[])raw[1],
      Global.getProductMap(),
      (TRewardPlan)raw[2]
    );

    double totalReward = dataModel.calcTotalReward();
    System.out.println("Total reward: " + totalReward);
  }

  private void loadCustomerTable() {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;
    try {
      //productLoader.setLocation(path);
      table = tableLoader.load();
      TreeTableViewWithFilterCtrl tblCtrl = tableLoader.getController();
      tblCtrl.setup(
//        "customerTable.toolbar.heading",
        "customerTable.toolbar.refresh",
        "customerTable.toolbar.searchPrompt",
        "customerTable.toolbar.filter",
        "customerTable.emptyPlaceHolder",
        c -> {
          Customer customer = (Customer)c;
          Set<String> strs = new HashSet<>();
          strs.addAll(Arrays.asList(
            customer.getName(),
            customer.getUid(),
            customer.getMobile()
          ));
          return strs;
        }
      );

      tblCtrl.setupColumsAndLoadData(
        tbl -> {
          JFXTreeTableView<Customer> theTable = (JFXTreeTableView<Customer>)tbl;
          theTable.getColumns().addAll(
//            TableViewHelper.jfxTableColumnResBundle(
//              "customerTable.uid",
//              Global.AllRes,
//              100,
//              Customer::getUid
//            ),
            TableViewHelper.jfxTableColumnResBundle(
              "customerTable.name",
              Global.AllRes,
              100,
              Customer::getName
            ),
            TableViewHelper.jfxTableColumnResBundle(
              "customerTable.mobile",
              Global.AllRes,
              150,
              Customer::getMobile
            )
          );

          UIHelpers.setPlaceHolder4TreeView(theTable, "customerTable.placeHolder");

        },
        () -> dataModel.getCustomers()
      );

      //StackedBarChart<String, Number> barChart = ChartHelpers.createChart(dataModel.getOrderData());
      System.out.println("data :" + dataModel.getOrderData().length);
      StackedBarChart<String, Number> barChart = ChartHelpers.createChartFromCustomerOrders(
        dataModel.getOrderData()
      );
      customersTab.getChildren().addAll(table, barChart);
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }

  private void loadCustomerOrderTable() {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;
    try {
      //productLoader.setLocation(path);
      table = tableLoader.load();
      TreeTableViewWithFilterCtrl tblCtrl = tableLoader.getController();
      tblCtrl.setup(
//        "customerOrderTable.toolbar.heading",
        "customerOrderTable.toolbar.refresh",
        "customerOrderTable.toolbar.searchPrompt",
        "customerOrderTable.toolbar.filter",
        "customerOrderTable.emptyPlaceHolder",
        c -> {
          CustomerOrder customerOrder = (CustomerOrder)c;
          Set<String> strs = new HashSet<>();
          strs.addAll(Arrays.asList(
            customerOrder.getCustomerName(),
            customerOrder.getOrder().getProdName()
          ));
          return strs;
        }
      );

      tblCtrl.setupColumsAndLoadData(
        tbl -> {
          JFXTreeTableView<CustomerOrder> theTable = (JFXTreeTableView<CustomerOrder>)tbl;
          theTable.getColumns().addAll(
            TableViewHelper.<CustomerOrder, String>jfxTableColumnResBundle(
              "refedCustomerOrderTable.customerId",
              Global.AllRes,
              150,
              CustomerOrder::getCustomerName
            ),
            TableViewHelper.<CustomerOrder, String>jfxTableColumnResBundle(
              "refedCustomerOrderTable.productName",
              Global.AllRes,
              320,
              (CustomerOrder co) -> co.getOrder().getProdName()
            ),
            TableViewHelper.<CustomerOrder, Double>jfxTableColumnResBundle(
              "refedCustomerOrderTable.productQty",
              Global.AllRes,
              80,
              (CustomerOrder co) -> co.getOrder().getQty()
            ),
            TableViewHelper.<CustomerOrder, Double>jfxTableColumnResBundle(
              "refedCustomerOrderTable.reward",
              Global.AllRes,
              80,
              CustomerOrder::getReward
            )
          );

          UIHelpers.setPlaceHolder4TreeView(theTable, "refedCustomerOrderTable.placeHolder");

        },
        () -> dataModel.getCustomerOrders()
      );

      ordersTab.getChildren().addAll(table);
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }

  @PostConstruct
  public void launch() {
    System.out.println("in @PostConstruct");
    //setupAndFetchCustomerTable();

    //setupAndFetchCustomerOrderTable();
    loadDataModel();
    loadCustomerOrderTable();
    loadCustomerTable();

  }
}
