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
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
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

  private ObjectProperty<Customer> selectedCustomer = new SimpleObjectProperty<>();

  private void loadCustomerTable() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;

    //productLoader.setLocation(path);
    table = tableLoader.load();
    customerCtrl = tableLoader.getController();
    customerCtrl.setup(
//        "customerTable.toolbar.heading",
      "customerTable.toolbar.refresh",
      "customerTable.toolbar.searchPrompt",
      "customerTable.toolbar.filter",
      "customerTable.emptyPlaceHolder",
      customer -> {
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          customer.getName(),
          customer.getUid(),
          customer.getMobile()
        ));
        return strs;
      }
    );

    customerCtrl.setupColumsAndLoadData(
      theTable -> {
        theTable.getColumns().addAll(
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
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "customerTable.pricePlanInfo",
            Global.AllRes,
            300,
            Customer::getPricePlanInfo
          )
        );

        UIHelpers.setPlaceHolder4TreeView(theTable, "customerTable.placeHolder");

      },
      () -> dataModel.getCustomers(),
      () -> {
        double m = DataUtils.chartMaxY(createBarChartsAll(), 100);
        maxChartValue.setValue(m);
        selectedCustomer.bind(customerCtrl.getSelected());
      }
    );

    selectedCustomer.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        String customerId = newValue.getUid();
        System.out.println("current customer " + customerId);
        createBarChartCurrCustomer(customerId, newValue.getName());
      }
    });

    //StackedBarChart<String, Number> barChart = ChartHelpers.createChart(dataModel.getOrderData());
    //System.out.println("data :" + dataModel.getOrderData().length);
    customersTab.getChildren().addAll(table);
  }

  @FXML
  StackPane productsTab;
  private TreeTableViewWithFilterCtrl<Product> productCtrl;
  private ObjectProperty<Product> selectedProduct = new SimpleObjectProperty<>();

  private void loadProductTable() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;

    //productLoader.setLocation(path);
    table = tableLoader.load();
    productCtrl = tableLoader.getController();
    productCtrl.setup(
//        "customerTable.toolbar.heading",
      "customerTable.toolbar.refresh",
      "customerTable.toolbar.searchPrompt",
      "customerTable.toolbar.filter",
      "customerTable.emptyPlaceHolder",
      product -> {
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          product.getName(),
          product.getDetailedInfo()
        ));
        return strs;
      }
    );

    productCtrl.setupColumsAndLoadData(
      theTable -> {
        theTable.getColumns().addAll(
          TableViewHelper.jfxTableColumnResBundle(
            "customerTable.name",
            Global.AllRes,
            100,
            Product::getName
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "customerTable.mobile",
            Global.AllRes,
            150,
            Product::getPrice0
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "customerTable.pricePlanInfo",
            Global.AllRes,
            300,
            Product::getDetailedInfo
          )
        );

        UIHelpers.setPlaceHolder4TreeView(theTable, "customerTable.placeHolder");

      },
      () -> dataModel.getProducts(),
      () -> {
//        double m = DataUtils.chartMaxY(createBarChartsAll(), 100);
//        maxChartValue.setValue(m);
        selectedProduct.bind(productCtrl.getSelected());
      }
    );

    selectedProduct.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        System.out.println(newValue.getName());
//        String customerId = newValue.getUid();
//        System.out.println("current customer " + customerId);
//        createBarChartCurrCustomer(customerId, newValue.getName());
      }
    });

    //StackedBarChart<String, Number> barChart = ChartHelpers.createChart(dataModel.getOrderData());
    //System.out.println("data :" + dataModel.getOrderData().length);
    productsTab.getChildren().addAll(table);
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
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;

    //productLoader.setLocation(path);
    table = tableLoader.load();
    pricePlanCtrl = tableLoader.getController();

    pricePlanCtrl.setup(
//        "customerTable.toolbar.heading",
      "pricePlanTable.toolbar.refresh",
      "pricePlanTable.toolbar.searchPrompt",
      "pricePlanTable.toolbar.filter",
      "pricePlanTable.emptyPlaceHolder",
      pricePlan -> {
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          pricePlan.getInfo()
        ));
        return strs;
      }
    );

    pricePlanCtrl.setupColumsAndLoadData(
      theTable -> {
        theTable.getColumns().addAll(
//            TableViewHelper.jfxTableColumnResBundle(
//              "customerTable.uid",
//              Global.AllRes,
//              100,
//              Customer::getUid
//            ),
          TableViewHelper.jfxTableColumnResBundle(
            "pricePlanTable.type",
            Global.AllRes,
            200,
            PricePlan::getVtag
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "pricePlanTable.info",
            Global.AllRes,
            400,
            PricePlan::getInfo
          )
        );

        UIHelpers.setPlaceHolder4TreeView(theTable, "pricePlanTable.placeHolder");

      },
      () -> dataModel.getPricePlans(),
      () -> {
//        double m = DataUtils.chartMaxY(createBarChartsAll(), 100);
//        maxChartValue.setValue(m);
//        selectedCustomer.bind(customerCtrl.getSelected());
      }
    );

    //StackedBarChart<String, Number> barChart = ChartHelpers.createChart(dataModel.getOrderData());
    //System.out.println("data :" + dataModel.getOrderData().length);
    pricePlanTab.getChildren().addAll(table);
  }
}
