package org.xg.ui.mainwnd;

import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.ViewController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.xg.dbModels.MProfOrgAgent;
import org.xg.ui.UiLoginController;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
import org.xg.ui.model.MedProfOrgDataModel;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiModels.ProfOrgAgent;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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
            Global.AllRes,
            100,
            ProfOrgAgent::getName
          ),
          TableViewHelper.jfxTableColumnResBundle(
            "orgAgentTable.mobile",
            Global.AllRes,
            150,
            ProfOrgAgent::getPhone
          )
        );

        UIHelpers.setPlaceHolder4TreeView(theTable, "orgAgentTable.placeHolder");

      },
      () -> dataModel.getAgents(),
      () -> {
//        double m = createBarChartsAll();
//        maxChartValue.setValue(m);
//        selectedCustomer.bind(customerCtrl.getSelected());
      }
    );

    selectedAgent.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        String customerId = newValue.getAgentId();
        //createBarChartCurrCustomer(customerId, newValue.getName());
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
    String agentId = Global.getCurrUid();
    String token = Global.getCurrToken();
    Object[] raw = Helpers.paraActions(
      new Supplier[] {
        () -> UISvcHelpers.updateProfOrgAgentsOf(agentId, token)
      },
      30000
    );

    dataModel = new MedProfOrgDataModel(
      (MProfOrgAgent[])raw[0]
    );

//    double totalReward = dataModel.calcTotalReward();
//    System.out.println("Total reward: " + totalReward);
  }

  @PostConstruct
  public void launch() {
    System.out.println("in @PostConstruct");
    //setupAndFetchCustomerTable();

    //setupAndFetchCustomerOrderTable();
    try {
      loadDataModel();

      loadAgentTable();

    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }
}
