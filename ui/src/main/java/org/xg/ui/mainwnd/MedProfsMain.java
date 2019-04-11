package org.xg.ui.mainwnd;

import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.xg.chart.ChartHelpers;
import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MOrder;
import org.xg.dbModels.MPricePlan;
import org.xg.gnl.DataUtils;
import org.xg.pay.pricePlan.TPricePlan;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.UiLoginController;
import org.xg.ui.comp.AddNewCustomerCtrl;
import org.xg.ui.comp.TreeTableViewHelper;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
import org.xg.ui.comp.UpdatePasswordCtrl;
import org.xg.uiModels.Customer;
import org.xg.uiModels.CustomerOrder;
import org.xg.ui.model.MedProfsDataModel;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiModels.PricePlan;
import org.xg.uiModels.Product;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;

@ViewController(value = "/ui/MedProfsMain.fxml")
public class MedProfsMain {

  @FXML
  private StackPane ordersTab;

  @FXML
  private StackPane customersTab;

  private MedProfsDataModel dataModel = null;

  private void loadDataModel() {
    String profId = Global.getCurrUid();
    String token = Global.getCurrToken();
    Object[] raw = Helpers.paraActions(
      new Supplier[] {
        () -> UISvcHelpers.updateAllRefedCustomers(profId, token),
        () -> UISvcHelpers.updateAllOrdersOfRefedCustomers(profId, token),
        () -> UISvcHelpers.updatePricePlansOfProf(profId, token),
        () -> UISvcHelpers.updatePricePlans4Customers(profId, token),
        () -> UISvcHelpers.updateRewardPlans(profId, token)
      },
      30000
    );

    dataModel = new MedProfsDataModel(
      (MCustomer[])raw[0],
      (MOrder[])raw[1],
      (MPricePlan[])raw[2],
      Global.getProductMap(),
      (Map<String, MPricePlan>)raw[3],
      (TRewardPlan)raw[4]
    );

    double totalReward = dataModel.calcTotalReward();
    System.out.println("Total reward: " + totalReward);
  }

  private TreeTableViewWithFilterCtrl<Customer> customerCtrl;

  private void loadCustomerTable() throws Exception {

    customerCtrl = TreeTableViewHelper.loadTableToTab(
      customersTab,
      customer -> {
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          customer.getName(),
          customer.getUid(),
          customer.getMobile()
        ));
        return strs;
      },
      () -> dataModel.getCustomers(),
      () -> {
        double m = DataUtils.chartMaxY(createBarChartsAll(), 100);
        maxChartValue.setValue(m);
      },
      () -> {
        System.out.println("todo");
      },
      "customerTable",
      "customerTable.placeHolder",
      newCustomer -> {
        String customerId = newCustomer.getUid();
//        System.out.println("current customer " + customerId);
//        System.out.println("maxChartValue " + maxChartValue.getValue());
        createBarChartCurrCustomer(customerId, newCustomer.getName());
      },
      Arrays.asList(
        TableViewHelper.jfxTableColumnResBundle(
          "customerTable.name",
          100,
          Customer::getName
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "customerTable.mobile",
          150,
          Customer::getMobile
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "customerTable.pricePlanInfo",
          300,
          Customer::getPricePlanInfo
        )
      )
    );
  }

  @FXML
  StackPane productsTab;
  private TreeTableViewWithFilterCtrl<Product> productCtrl;

  private void loadProductTable() throws Exception {

    productCtrl = TreeTableViewHelper.loadTableToTab(
      productsTab,
      (Product product) -> {
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          product.getName(),
          product.getDetailedInfo()
        ));
        return strs;
      },
      () -> dataModel.getProducts(),
      () -> {
        System.out.println("todo");
      },
      "customerTable",
      "customerTable.placeHolder",
      selectedProduct -> {
        System.out.println(selectedProduct.getName());
      },
      Arrays.asList(
        TableViewHelper.jfxTableColumnResBundle(
          "customerTable.name",
          100,
          Product::getName
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "customerTable.mobile",
          150,
          Product::getPrice0
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "customerTable.pricePlanInfo",
          300,
          Product::getDetailedInfo
        )
      )

    );

  }

  @FXML
  private VBox vboxChartAll;
  @FXML
  private VBox vboxChartCurrCustomer;

  private DoubleProperty maxChartValue = new SimpleDoubleProperty();

  private double createBarChartsAll() {
    vboxChartAll.getChildren().clear();

    StackedBarChart<String, Number> barChartAll = ChartHelpers.createChartFromCustomerOrders(
      dataModel.getOrderData(),
      new String[] {
        Global.AllRes.getString("customerBarChart.category.paid"),
        Global.AllRes.getString("customerBarChart.category.unpaid"),
      },
      Global.AllRes.getString("customerBarChart.title"),
      null
    );
    barChartAll.setMaxHeight(350);

    vboxChartAll.getChildren().addAll(barChartAll);

    List<Double> sums = new ArrayList<>();
    for (int i = 0; i < barChartAll.getData().size(); i++) {
      XYChart.Series<String, Number> d = barChartAll.getData().get(i);
      for (int j = 0; j < d.getData().size(); j++) {
        double curr = d.getData().get(j).getYValue().doubleValue();
        if (sums.size() <= j) {
          sums.add(curr);
        }
        else {
          sums.set(j, sums.get(j)+curr);
        }
      }
    }
    if (sums.size() > 0) {
      double max = sums.stream().max(Double::compareTo).get();

      return max;
    }
    else
      return 0.0;
  }

  private void createBarChartCurrCustomer(String currCustomerId, String currCustomerName) {
    vboxChartCurrCustomer.getChildren().clear();

    CustomerOrder[] currCustomerOrders = Arrays.stream(dataModel.getOrderData())
      .filter(o -> o.getCustomerId().equals(currCustomerId)).toArray(CustomerOrder[]::new);
    StackedBarChart<String, Number> barChartCurrUser = ChartHelpers.createChartFromCustomerOrders(
      currCustomerOrders,
      new String[] {
        Global.AllRes.getString("customerBarChart.category.paid"),
        Global.AllRes.getString("customerBarChart.category.unpaid"),
      },
      String.format(
        "%s(%s)", Global.AllRes.getString("customerBarChart.title"), currCustomerName
      ),
      maxChartValue.getValue()
    );
    barChartCurrUser.setMaxHeight(350);

    vboxChartCurrCustomer.getChildren().addAll(barChartCurrUser);

  }

  private void loadCustomerOrderTable() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;

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
            150,
            CustomerOrder::getCustomerName
          ),
          TableViewHelper.<CustomerOrder, String>jfxTableColumnResBundle(
            "refedCustomerOrderTable.productName",
            320,
            (CustomerOrder co) -> co.getOrder().getProdName()
          ),
          TableViewHelper.<CustomerOrder, Double>jfxTableColumnResBundle(
            "refedCustomerOrderTable.productQty",
            80,
            (CustomerOrder co) -> co.getOrder().getQty()
          ),
          TableViewHelper.<CustomerOrder, Double>jfxTableColumnResBundle(
            "refedCustomerOrderTable.reward",
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

  @PostConstruct
  public void launch() {
    System.out.println("in @PostConstruct");
    //setupAndFetchCustomerTable();

    //setupAndFetchCustomerOrderTable();
    try {
      loadDataModel();
      loadCustomerOrderTable();
      loadProductTable();
      loadCustomerTable();
      loadPricePlanTable();
      loadAddNewCustomerTab();
      loadUpdatePasswordTab();
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }

  @FXML
  VBox addNewCustomerTab;

  private AddNewCustomerCtrl addNewCustomerCtrl;
  private void loadAddNewCustomerTab() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/AddNewCustomer.fxml");
    FXMLLoader addNewCustomerLoader = new FXMLLoader(path, Global.AllRes);
    VBox addNewProf = addNewCustomerLoader.load();
    addNewCustomerCtrl = addNewCustomerLoader.getController();

    addNewCustomerCtrl.setup(
      dataModel.getPricePlanOptions(),
      () -> updateCustomers()
    );

    addNewCustomerTab.getChildren().add(addNewProf);
  }

  private void updateCustomers() {
    String profId = Global.getCurrUid();
    String token = Global.getCurrToken();
    Object[] raw = Helpers.paraActions(
      new Supplier[] {
        () -> UISvcHelpers.updateAllRefedCustomers(profId, token),
        () -> UISvcHelpers.updatePricePlans4Customers(profId, token)
      },
      30000
    );

    dataModel.setCustomers((MCustomer[]) raw[0], (Map<String, MPricePlan>) raw[1]);

    customerCtrl.filterAndUpdateTable2(dataModel.getCustomers(), t -> true);
  }


  @FXML
  VBox pricePlanTab;
  TreeTableViewWithFilterCtrl<PricePlan> pricePlanCtrl;

  private void loadPricePlanTable() throws Exception {
    pricePlanCtrl = TreeTableViewHelper.loadTableToTab(
      pricePlanTab,
      pricePlan -> {
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          pricePlan.getInfo()
        ));
        return strs;
      },
      () -> dataModel.getPricePlans(),
      () -> {
        System.out.println("todo");
      },
      "pricePlanTable",
      "pricePlanTable.placeHolder",
      selected -> { },
      Arrays.asList(
        TableViewHelper.jfxTableColumnResBundle(
          "pricePlanTable.type",
          200,
          PricePlan::getVtag
        ),
        TableViewHelper.jfxTableColumnResBundle(
          "pricePlanTable.info",
          400,
          PricePlan::getInfo
        )
      )
    );
  }

  @FXML
  VBox updatePasswordTab;
  private void loadUpdatePasswordTab() throws Exception {
    UpdatePasswordCtrl.load2Tab(updatePasswordTab);
  }
}
