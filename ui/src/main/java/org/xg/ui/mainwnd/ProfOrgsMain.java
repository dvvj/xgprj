package org.xg.ui.mainwnd;

import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.xg.dbModels.MMedProf;
import org.xg.ui.UiLoginController;
import org.xg.ui.comp.TreeTableViewWithFilterCtrl;
import org.xg.ui.model.Customer;
import org.xg.ui.model.MedProf;
import org.xg.ui.model.ProfOrgsDataModel;
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

@ViewController(value = "/ui/ProfOrgsMain.fxml")
public class ProfOrgsMain {
  @FXML
  private StackPane orderStatsTab;

  private void loadOrderStatsTab() {
    Text txtTmp = new Text();
    txtTmp.setText("ok");
    orderStatsTab.getChildren().add(txtTmp);
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
      },
      30000
    );
    dataModel = new ProfOrgsDataModel(
      (MMedProf[])raw[0]
    );
  }
}
