package org.xg.ui.mainwnd;

import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.ui.UiLoginController;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@ViewController(value = "/ui/ProfOrgsMain.fxml")
public class ProfOrgsMain {
  @FXML
  private StackPane orderStatsTab;

  private void loadOrderStatsTab() {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;
    try {
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
        () -> dataModel.getOrderStats()
      );

      orderStatsTab.getChildren().addAll(table);
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }

  private void loadMedProfsTab() {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;
    try {
      //productLoader.setLocation(path);
      table = tableLoader.load();
      TreeTableViewWithFilterCtrl tblCtrl = tableLoader.getController();
      tblCtrl.setup(
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

      tblCtrl.setupColumsAndLoadData(
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
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }

  @FXML
  private StackPane profsTab;


  @PostConstruct
  public void launch() {
    System.out.println("in post construct");
    loadDataModel();
    loadOrderStatsTab();
    loadMedProfsTab();
  }

  private ProfOrgsDataModel dataModel = null;

  private void loadDataModel() {
    String orgId = Global.getCurrUid();
    String token = Global.getCurrToken();

    Object[] raw = Helpers.paraActions(
      new Supplier[] {
        () -> UISvcHelpers.updateMedProfsOf(orgId, token),
        () -> UISvcHelpers.updateOrgOrderStats(orgId, token)
      },
      30000
    );

    MMedProf[] profs = (MMedProf[])raw[0];
    System.out.println("Profs found: " + profs.length);
    dataModel = new ProfOrgsDataModel(
      profs,
      (MOrgOrderStat[])raw[1]
    );
  }
  @FXML
  private VBox vboxChartAll;
  @FXML
  private VBox vboxChartCurrProf;


}
