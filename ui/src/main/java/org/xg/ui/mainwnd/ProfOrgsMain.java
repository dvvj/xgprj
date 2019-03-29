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
import javafx.scene.text.Text;
import org.xg.chart.ChartHelpers;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.pay.rewardPlan.TRewardPlan;
import org.xg.ui.UiLoginController;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
import org.xg.uiModels.Customer;
import org.xg.uiModels.MedProf;
import org.xg.ui.model.ProfOrgsDataModel;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiModels.OrgOrderStat;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@ViewController(value = "/ui/ProfOrgsMain.fxml")
public class ProfOrgsMain {
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
        OrgOrderStat orderStat = (OrgOrderStat) c;
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
        JFXTreeTableView<OrgOrderStat> theTable = (JFXTreeTableView<OrgOrderStat>)tbl;
        theTable.getColumns().addAll(
//            TableViewHelper.jfxTableColumnResBundle(
//              "customerTable.uid",
//              Global.AllRes,
//              100,
//              Customer::getUid
//            ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderStatsTable.prodName",
            Global.AllRes,
            150,
            OrgOrderStat::getProdName
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderStatsTable.qty",
            Global.AllRes,
            100,
            OrgOrderStat::getQty
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderStatsTable.actualCost",
            Global.AllRes,
            100,
            OrgOrderStat::getActualCost
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orderStatsTable.profName",
            Global.AllRes,
            100,
            OrgOrderStat::getProfName
          )
        );

        UIHelpers.setPlaceHolder4TreeView(theTable, "orderStatsTable.placeHolder");

      },
      () -> dataModel.getOrderStats(),
      () -> {
        double m = ((int)(createCostBarChartsAll() / 500)) * 500;
        maxCostChartValue.setValue(m);
        m = ((int)(createRewardBarChartsAll() / 500)) * 500;
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
            Global.AllRes,
            100,
            MedProf::getName
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "medprofsTable.mobile",
            Global.AllRes,
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
      loadAddNewProfsTab();
      loadUpdatePasswordTab();
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private ProfOrgsDataModel dataModel = null;

  private void loadDataModel() {
    String orgId = Global.getCurrUid();
    String token = Global.getCurrToken();

    Object[] raw = Helpers.paraActions(
      new Supplier[] {
        () -> UISvcHelpers.updateMedProfsOf(orgId, token),
        () -> UISvcHelpers.updateOrgOrderStats(orgId, token),
        () -> UISvcHelpers.updateRewardPlans(orgId, token)
      },
      30000
    );

    MMedProf[] profs = (MMedProf[])raw[0];
    System.out.println("Profs found: " + profs.length);
    dataModel = new ProfOrgsDataModel(
      profs,
      (MOrgOrderStat[])raw[1],
      Global.getProductMap(),
      (TRewardPlan)raw[2]
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
      OrgOrderStat::getActualCost
    );
  }

  @FXML
  private VBox vboxRewardChartAll;

  private double createBarChartsAll(
    VBox barChartParent,
    String titleRes,
    Function<OrgOrderStat, Double> resultGetter
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
      OrgOrderStat::getReward
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
      OrgOrderStat::getActualCost
    );
  }

  private void updateBarChartCurrProf(
    String profId, String profName,
    VBox barChartParent,
    String titleRes,
    Function<OrgOrderStat, Double> resultGetter
  ) {
    barChartParent.getChildren().clear();

    OrgOrderStat[] orgOrderStats = Arrays.stream(dataModel.getRawOrderStats())
      .filter(o -> o.getProfId().equals(profId)).toArray(OrgOrderStat[]::new);
    StackedBarChart<String, Number> barChartCurrProf = ChartHelpers.createChartFromOrderStats(
      orgOrderStats,
      new String[] {
        Global.AllRes.getString("orderStatsBarChart.category.paid"),
        Global.AllRes.getString("orderStatsBarChart.category.unpaid"),
      },
      String.format(
        "%s(%s)", Global.AllRes.getString(titleRes), profName
      ),
      maxCostChartValue.getValue(),
      resultGetter
    );
    barChartCurrProf.setMaxHeight(350);

    barChartParent.getChildren().addAll(barChartCurrProf);
  }

  @FXML
  private VBox vboxRewardChartCurrProf;

  private void createRewardBarChartCurrProf(String profId, String profName) {
    updateBarChartCurrProf(
      profId, profName,
      vboxRewardChartCurrProf,
      "orderStatsRewardBarChart.title",
      OrgOrderStat::getReward
    );
  }

  @FXML
  VBox addNewProfsTab;
  private void loadAddNewProfsTab() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/AddNewMedProf.fxml");
    FXMLLoader addNewProfLoader = new FXMLLoader(path, Global.AllRes);
    VBox addNewProf = addNewProfLoader.load();


    addNewProfsTab.getChildren().addAll(addNewProf);

  }
  @FXML
  VBox updatePasswordTab;
  private void loadUpdatePasswordTab() throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/UpdatePassword.fxml");
    FXMLLoader updatePassLoader = new FXMLLoader(path, Global.AllRes);
    VBox updatePass = updatePassLoader.load();

    updatePasswordTab.getChildren().addAll(updatePass);

  }
}
