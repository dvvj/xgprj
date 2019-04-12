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
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MOrgAgentOrderStat;
import org.xg.dbModels.MRewardPlan;
import org.xg.gnl.DataUtils;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.UiLoginController;
import org.xg.ui.comp.AddNewMedProfCtrl;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
import org.xg.ui.comp.UpdatePasswordCtrl;
import org.xg.uiModels.MedProf;
import org.xg.ui.model.ProfOrgAgentDataModel;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiModels.OrgAgentOrderStat;
import org.xg.uiModels.RewardPlan;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@ViewController(value = "/ui/ProfOrgAgentMain.fxml")
public class ProfOrgAgentsMain {
  @FXML
  private StackPane orderStatsTab;

  private TreeTableViewWithFilterCtrl<MedProf> profCtrl;

  private void loadOrderStatsTab() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;

    //productLoader.setLocation(path);
    table = tableLoader.load();
    TreeTableViewWithFilterCtrl tblCtrl = tableLoader.getController();
    tblCtrl.setup(
//        "customerTable.toolbar.heading",
      "orderStatsTable.toolbar.refresh",
      "orderStatsTable.toolbar.searchPrompt",
      "orderStatsTable.toolbar.filter",
      "orderStatsTable.emptyPlaceHolder",
      c -> {
        OrgAgentOrderStat orderStat = (OrgAgentOrderStat) c;
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          orderStat.getProfId(),
          orderStat.getProdName()
        ));
        return strs;
      }
    );

    tblCtrl.setupColumsAndLoadData(
      tbl -> {
        JFXTreeTableView<OrgAgentOrderStat> theTable = (JFXTreeTableView<OrgAgentOrderStat>)tbl;
        theTable.getColumns().addAll(
//            TableViewHelper.jfxTableColumnResBundle(
//              "customerTable.uid",
//              Global.AllRes,
//              100,
//              Customer::getUid
//            ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderStatsTable.prodName",
            150,
            OrgAgentOrderStat::getProdName
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderStatsTable.qty",
            100,
            OrgAgentOrderStat::getQty
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderStatsTable.actualCost",
            100,
            OrgAgentOrderStat::getActualCost
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderStatsTable.profName",
            100,
            OrgAgentOrderStat::getProfName
          )
        );

        UIHelpers.setPlaceHolder4TreeView(theTable, "orderStatsTable.placeHolder");

      },
      () -> dataModel.getOrderStats(),
      () -> {
        double m = DataUtils.chartMaxY(createCostBarChartsAll(), 500);
        maxCostChartValue.setValue(m);
        m = DataUtils.chartMaxY(createRewardBarChartsAll(), 200);
        maxRewardChartValue.setValue(m);
        selectedProf.bind(profCtrl.getSelected());
      }
    );
    selectedProf.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        String profId = newValue.getUid();
        createCostBarChartCurrProf(profId, newValue.getName());
        createRewardBarChartCurrProf(profId, newValue.getName());
      }
    });
    orderStatsTab.getChildren().addAll(table);

  }
  private DoubleProperty maxCostChartValue = new SimpleDoubleProperty();
  private DoubleProperty maxRewardChartValue = new SimpleDoubleProperty();

  private void loadMedProfsTab() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;

    //productLoader.setLocation(path);
    table = tableLoader.load();
    profCtrl = tableLoader.getController();
    profCtrl.setup(
//        "customerTable.toolbar.heading",
      "medprofsTable.toolbar.refresh",
      "medprofsTable.toolbar.searchPrompt",
      "medprofsTable.toolbar.filter",
      "medprofsTable.emptyPlaceHolder",
      c -> {
        MedProf medProf = (MedProf) c;
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          medProf.getName(),
          medProf.getUid(),
          medProf.getMobile()
        ));
        return strs;
      }
    );

    profCtrl.setupColumsAndLoadData(
      tbl -> {
        JFXTreeTableView<MedProf> theTable = (JFXTreeTableView<MedProf>)tbl;
        theTable.getColumns().addAll(
//            TableViewHelper.jfxTableColumnResBundle(
//              "customerTable.uid",
//              Global.AllRes,
//              100,
//              Customer::getUid
//            ),
          TableViewHelper.jfxTableColumnResBundle(
            "medprofsTable.name",
            100,
            MedProf::getName
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "medprofsTable.mobile",
            150,
            MedProf::getMobile
          )
        );

        UIHelpers.setPlaceHolder4TreeView(theTable, "medprofsTable.placeHolder");

      },
      () -> dataModel.getMedProfs()
    );

    profsTab.getChildren().addAll(table);

  }

  @FXML
  private StackPane profsTab;


  @PostConstruct
  public void launch() {
    try {
      System.out.println("in post construct");
      loadDataModel();
      loadOrderStatsTab();
      loadMedProfsTab();
      loadRewardPlansTab();
      loadAddNewProfsTab();
      loadUpdatePasswordTab();
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private ProfOrgAgentDataModel dataModel = null;

  private void loadDataModel() {
    String orgAgentId = Global.getCurrUid();
    String token = Global.getCurrToken();

    Object[] raw = Helpers.paraActions(
      new Supplier[] {
        () -> UISvcHelpers.updateMedProfsOf(orgAgentId, token),
        () -> UISvcHelpers.updateOrgAgentOrderStats(orgAgentId, token),
        () -> UISvcHelpers.updateRewardPlansCreatedBy(orgAgentId, token),
        () -> UISvcHelpers.updateRewardPlans(orgAgentId, token)
      },
      30000
    );

    MMedProf[] profs = (MMedProf[])raw[0];
    System.out.println("Profs found: " + profs.length);
    dataModel = new ProfOrgAgentDataModel(
      profs,
      (MOrgAgentOrderStat[])raw[1],
      (MRewardPlan[])raw[2],
      Global.getProductMap(),
      (TRewardPlan)raw[3]
    );

    double totalReward = dataModel.calcTotalReward();
    System.out.println("Total reward: " + totalReward);
  }
  @FXML
  private VBox vboxCostChartAll;

  private double createCostBarChartsAll() {
    return createBarChartsAll(
      vboxCostChartAll,
      "orderStatsCostBarChart.title",
      OrgAgentOrderStat::getActualCost
    );
  }

  @FXML
  private VBox vboxRewardChartAll;

  private double createBarChartsAll(
    VBox barChartParent,
    String titleRes,
    Function<OrgAgentOrderStat, Double> resultGetter
  ) {
    barChartParent.getChildren().clear();

    StackedBarChart<String, Number> barChartAll = ChartHelpers.createChartFromOrderStats(
      dataModel.getRawOrderStats(),
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

  private double createRewardBarChartsAll() {
    return createBarChartsAll(
      vboxRewardChartAll,
      "orderStatsRewardBarChart.title",
      OrgAgentOrderStat::getReward
    );
  }



  @FXML
  private VBox vboxCostChartCurrProf;

  private ObjectProperty<MedProf> selectedProf = new SimpleObjectProperty<>();

  private void createCostBarChartCurrProf(String profId, String profName) {
    updateBarChartCurrProf(
      profId, profName,
      vboxCostChartCurrProf,
      "orderStatsCostBarChart.title",
      OrgAgentOrderStat::getActualCost,
      maxCostChartValue.getValue()
    );
  }

  private void updateBarChartCurrProf(
    String profId, String profName,
    VBox barChartParent,
    String titleRes,
    Function<OrgAgentOrderStat, Double> resultGetter,
    double maxValue
  ) {
    barChartParent.getChildren().clear();

    OrgAgentOrderStat[] orgAgentOrderStats = Arrays.stream(dataModel.getRawOrderStats())
      .filter(o -> o.getProfId().equals(profId)).toArray(OrgAgentOrderStat[]::new);
    StackedBarChart<String, Number> barChartCurrProf = ChartHelpers.createChartFromOrderStats(
      orgAgentOrderStats,
      new String[] {
        Global.AllRes.getString("orderStatsBarChart.category.paid"),
        Global.AllRes.getString("orderStatsBarChart.category.unpaid"),
      },
      String.format(
        "%s(%s)", Global.AllRes.getString(titleRes), profName
      ),
      maxValue,
      resultGetter
    );
    //barChartCurrProf.setMaxHeight(350);

    barChartParent.getChildren().addAll(barChartCurrProf);
  }

  @FXML
  private VBox vboxRewardChartCurrProf;

  private void createRewardBarChartCurrProf(String profId, String profName) {
    updateBarChartCurrProf(
      profId, profName,
      vboxRewardChartCurrProf,
      "orderStatsRewardBarChart.title",
      OrgAgentOrderStat::getReward,
      maxRewardChartValue.getValue()
    );
  }

  @FXML
  VBox addNewProfsTab;

  private void loadAddNewProfsTab() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/AddNewMedProf.fxml");
    FXMLLoader addNewProfLoader = new FXMLLoader(path, Global.AllRes);
    VBox addNewProf = addNewProfLoader.load();
    AddNewMedProfCtrl ctrl = addNewProfLoader.getController();
    ctrl.setup(
      dataModel.getRewardPlanOptions(),
      () -> updateMedProfs()
    );

    addNewProfsTab.getChildren().addAll(addNewProf);

  }
  private void updateMedProfs() {
    String agentId = Global.getCurrUid();
    String token = Global.getCurrToken();

    profCtrl.updateDataNoFilter(
      () -> {
        Object[] raw = Helpers.paraActions(
          new Supplier[] {
            () -> UISvcHelpers.updateMedProfsOf(agentId, token)
          },
          10000
        );

        dataModel.setMedProfs((MMedProf[])raw[0]);
        return dataModel.getMedProfs();
      }
    );
  }

  @FXML
  VBox updatePasswordTab;
  private void loadUpdatePasswordTab() throws Exception {
    UpdatePasswordCtrl.load2Tab(updatePasswordTab);
//    URL path = UiLoginController.class.getResource("/ui/comp/UpdatePassword.fxml");
//    FXMLLoader updatePassLoader = new FXMLLoader(path, Global.AllRes);
//    VBox updatePass = updatePassLoader.load();
//
//    updatePasswordTab.getChildren().addAll(updatePass);
  }

  @FXML
  VBox rewardPlanTab;
  TreeTableViewWithFilterCtrl<RewardPlan> rewardPlanCtrl;
  private void loadRewardPlansTab() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;

    //productLoader.setLocation(path);
    table = tableLoader.load();
    rewardPlanCtrl = tableLoader.getController();

    rewardPlanCtrl.setup(
//        "customerTable.toolbar.heading",
      "rewardPlanTable.toolbar.refresh",
      "rewardPlanTable.toolbar.searchPrompt",
      "rewardPlanTable.toolbar.filter",
      "rewardPlanTable.emptyPlaceHolder",
      c -> {
        RewardPlan plan = c;
        Set<String> strs = new HashSet<>();
        strs.addAll(Arrays.asList(
          plan.getInfo()
        ));
        return strs;
      }
    );

    rewardPlanCtrl.setupColumsAndLoadData(
      theTable -> {
        theTable.getColumns().addAll(
//            TableViewHelper.jfxTableColumnResBundle(
//              "customerTable.uid",
//              Global.AllRes,
//              100,
//              Customer::getUid
//            ),
          TableViewHelper.jfxTableColumnResBundle(
            "rewardPlanTable.type",
            200,
            RewardPlan::getVtag
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "rewardPlanTable.info",
            400,
            RewardPlan::getInfo
          )
        );

        UIHelpers.setPlaceHolder4TreeView(theTable, "rewardPlanTable.placeHolder");

      },
      () -> dataModel.getOwnedRewardPlans()
    );

    rewardPlanTab.getChildren().addAll(table);

  }
}
