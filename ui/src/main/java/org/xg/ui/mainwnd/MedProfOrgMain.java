package org.xg.ui.mainwnd;

import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.ViewController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.xg.chart.ChartHelpers;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.dbModels.MProfOrgAgent;
import org.xg.dbModels.MRewardPlan;
import org.xg.dbModels.MRewardPlanMap;
import org.xg.gnl.DataUtils;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.UiLoginController;
import org.xg.ui.comp.AddNewProfOrgAgentCtrl;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
import org.xg.ui.comp.UpdatePasswordCtrl;
import org.xg.ui.model.MedProfOrgDataModel;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiModels.OrgAgentOrderStat;
import org.xg.uiModels.OrgOrderStat;
import org.xg.uiModels.ProfOrgAgent;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@ViewController(value = "/ui/MedProfOrgMain.fxml")
public class MedProfOrgMain {

  private MedProfOrgDataModel dataModel;

  private TreeTableViewWithFilterCtrl<ProfOrgAgent> agentCtrl;
  private void loadAgentTable() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;

    //productLoader.setLocation(path);
    table = tableLoader.load();
    agentCtrl = tableLoader.getController();
    TreeTableViewWithFilterCtrl tblCtrl = tableLoader.getController();
    tblCtrl.setup(
//        "customerTable.toolbar.heading",
      "orgAgentTable.toolbar.refresh",
      "orgAgentTable.toolbar.searchPrompt",
      "orgAgentTable.toolbar.filter",
      "orgAgentTable.emptyPlaceHolder",
      c -> {
        ProfOrgAgent agent = (ProfOrgAgent)c;
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          agent.getName(),
          agent.getAgentId(),
          agent.getPhone()
        ));
        return strs;
      }
    );

    tblCtrl.setupColumsAndLoadData(
      tbl -> {
        JFXTreeTableView<ProfOrgAgent> theTable = (JFXTreeTableView<ProfOrgAgent>)tbl;
        theTable.getColumns().addAll(
//            TableViewHelper.jfxTableColumnResBundle(
//              "customerTable.uid",
//              Global.AllRes,
//              100,
//              Customer::getUid
//            ),
          TableViewHelper.jfxTableColumnResBundle(
            "orgAgentTable.name",
            100,
            ProfOrgAgent::getName
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orgAgentTable.mobile",
            150,
            ProfOrgAgent::getPhone
          )
        );

        UIHelpers.setPlaceHolder4TreeView(theTable, "orgAgentTable.placeHolder");

      },
      () -> dataModel.getAgents(),
      () -> {
        double m = DataUtils.chartMaxY(createCostBarChartsAll(), 500);
        maxCostChartValue.setValue(m);
        m = DataUtils.chartMaxY(createRewardBarChartsAll(), 200);
        maxRewardChartValue.setValue(m);
        selectedAgent.bind(agentCtrl.getSelected());
      }
    );

    selectedAgent.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        String agentId = newValue.getAgentId();
        createCostBarChartCurrAgent(agentId, newValue.getName());
        createRewardBarChartCurrAgent(agentId, newValue.getName());
      }
    });

    //StackedBarChart<String, Number> barChart = ChartHelpers.createChart(dataModel.getOrderData());
    //System.out.println("data :" + dataModel.getOrderData().length);
    orgAgentsTab.getChildren().addAll(table);


  }

  @FXML
  StackPane orgAgentsTab;

  private ObjectProperty<ProfOrgAgent> selectedAgent = new SimpleObjectProperty<>();

  private void loadDataModel() {
    String orgId = Global.getCurrUid();
    String token = Global.getCurrToken();
    Object[] raw = Helpers.paraActions(
      new Supplier[] {
        () -> UISvcHelpers.updateProfOrgAgentsOf(token),
        () -> UISvcHelpers.updateOrgOrderStats(token),
        () -> UISvcHelpers.updateRewardPlans(token),
        () -> UISvcHelpers.updateAgentRewardPlans(token)
      },
      30000
    );

    dataModel = new MedProfOrgDataModel(
      (MProfOrgAgent[])raw[0],
      (MOrgOrderStat[])raw[1],
      Global.getProductMap(),
      (MRewardPlan[]) raw[2],
      (MRewardPlanMap[])raw[3]
    );

    double totalReward = dataModel.calcTotalReward();
    System.out.println("Total reward: " + totalReward);
  }

  @PostConstruct
  public void launch() {
    //System.out.println("in @PostConstruct");
    //setupAndFetchCustomerTable();

    //setupAndFetchCustomerOrderTable();
    try {
      loadDataModel();

      loadAgentTable();
      loadAddNewAgentTab();
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }

  @FXML
  private VBox vboxCostChartAll;

  private double createCostBarChartsAll() {
    return createBarChartsAll(
      vboxCostChartAll,
      "orderStatsCostBarChart.title",
      os -> os.getAgentOrderStat().getActualCost()
    );
  }

  private double createBarChartsAll(
    VBox barChartParent,
    String titleRes,
    Function<OrgOrderStat, Double> resultGetter
  ) {
    barChartParent.getChildren().clear();

    StackedBarChart<String, Number> barChartAll = ChartHelpers.createChartFromOrderStats4Org(
      dataModel.getRawOrderStat(),
      new String[] {
        Global.AllRes.getString("orderStatsBarChart.category.paid"),
        Global.AllRes.getString("orderStatsBarChart.category.unpaid"),
      },
      Global.AllRes.getString(titleRes),
      null,
      resultGetter
    );
    barChartAll.setMaxHeight(350);

    barChartParent.getChildren().addAll(barChartAll);

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

  private DoubleProperty maxCostChartValue = new SimpleDoubleProperty();
  private DoubleProperty maxRewardChartValue = new SimpleDoubleProperty();

  @FXML
  private VBox vboxCostChartCurrAgent;

  private void createCostBarChartCurrAgent(String profId, String profName) {
    updateBarChartCurrAgent(
      profId, profName,
      vboxCostChartCurrAgent,
      "orderStatsCostBarChart.title",
      o -> o.getAgentOrderStat().getActualCost(),
      maxCostChartValue.getValue()
    );
  }

  private void updateBarChartCurrAgent(
    String agentId, String agentName,
    VBox barChartParent,
    String titleRes,
    Function<OrgOrderStat, Double> resultGetter,
    double maxValue
  ) {
    barChartParent.getChildren().clear();

    OrgOrderStat[] orgOrderStats = Arrays.stream(dataModel.getRawOrderStat())
      .filter(o -> o.getAgentOrderStat().getOrgAgentId().equals(agentId)).toArray(OrgOrderStat[]::new);
    StackedBarChart<String, Number> barChartCurrAgent = ChartHelpers.createChartFromOrderStats4Org(
      orgOrderStats,
      new String[] {
        Global.AllRes.getString("orderStatsBarChart.category.paid"),
        Global.AllRes.getString("orderStatsBarChart.category.unpaid"),
      },
      String.format(
        "%s(%s)", Global.AllRes.getString(titleRes), agentName
      ),
      maxValue,
      resultGetter
    );
    //barChartCurrProf.setMaxHeight(350);

    barChartParent.getChildren().addAll(barChartCurrAgent);
  }

  @FXML
  private VBox vboxRewardChartAll;
  private double createRewardBarChartsAll() {
    return createBarChartsAll(
      vboxRewardChartAll,
      "orderStatsRewardBarChart.title",
      o -> o.getAgentOrderStat().getReward()
    );
  }
  @FXML
  private VBox vboxRewardChartCurrAgent;

  private void createRewardBarChartCurrAgent(String profId, String agentName) {
    updateBarChartCurrAgent(
      profId, agentName,
      vboxRewardChartCurrAgent,
      "orderStatsRewardBarChart.title",
      o -> o.getAgentOrderStat().getReward(),
      maxRewardChartValue.getValue()
    );
  }

  @FXML
  VBox updatePasswordTab;
  private void loadUpdatePasswordTab() throws Exception {
    UpdatePasswordCtrl.load2Tab(updatePasswordTab);
  }

  @FXML
  VBox addNewAgentTab;

  private void loadAddNewAgentTab() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/AddNewProfOrgAgent.fxml");
    FXMLLoader loader = new FXMLLoader(path, Global.AllRes);
    VBox vbAddNewAgent = loader.load();
//    AddNewProfOrgAgentCtrl ctrl = loader.getController();
//    ctrl.setup(
//      dataModel.getRewardPlanOptions(),
//      () -> updateMedProfs()
//    );

    addNewAgentTab.getChildren().addAll(vbAddNewAgent);

  }
}
