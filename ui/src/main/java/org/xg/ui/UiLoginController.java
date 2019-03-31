package org.xg.ui;

import io.datafx.controller.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.xg.ui.mainwnd.CustomerMain;
import org.xg.ui.mainwnd.MainFrame;
import org.xg.ui.mainwnd.MedProfsMain;
import org.xg.ui.mainwnd.ProfOrgAgentsMain;
import org.xg.ui.model.ComboOptionData;
import org.xg.ui.model.UserTypeHelpers;
import org.xg.ui.utils.Global;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ViewController(value = "/ui/UiLogin.fxml")
public class UiLoginController implements Initializable {
  @FXML
  private TextField tfUid;

  @FXML
  private Button btnLogin;

  @FXML
  private PasswordField pfPass;

  @FXML
  private Text txtStatus;

  @FXML
  private CheckBox chbRemember;

  @FXML
  private ComboBox<ComboOptionData> cmboUType;

  @FXML
  private void closeWindow() {
    final Stage stage = (Stage)cmboUType.getScene().getWindow();
    stage.close();
  }

  private Map<Integer, ComboOptionData> userMap;

  private Map<Integer, Runnable> loginSuccessActionMap;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    userMap = UserTypeHelpers.createMap(resources);

    //txtStatus.setText(GlobalCfg.currentDir());

    cmboUType.getItems().addAll(
      userMap.values()
    );

    cmboUType.setCellFactory(
      new Callback<ListView<ComboOptionData>, ListCell<ComboOptionData>>() {
        @Override
        public ListCell<ComboOptionData> call(ListView<ComboOptionData> param) {
          return new ListCell<ComboOptionData>() {
            @Override
            protected void updateItem(ComboOptionData item, boolean empty) {
              super.updateItem(item, empty);

              if (item == null || empty) {
                setGraphic(null);
              }
              else {
                Label lbl = new Label();
                lbl.setText(item.getName());
                setGraphic(lbl);
              }
            }
          };
        }
      }
    );

    cmboUType.setValue(
      userMap.get(UserTypeHelpers.UT_PROFORG_AGENT)
    );


    loginSuccessActionMap = new HashMap<>();
    loginSuccessActionMap.put(
      UserTypeHelpers.UT_CUSTOMER, () -> {
        //new CustomerMain().launch(tfUid.getText());
        new MainFrame("customer.main.title").start(CustomerMain.class);
        closeWindow();
      }
    );
    loginSuccessActionMap.put(
      UserTypeHelpers.UT_MEDPROFS, () -> {
        //new MedProfsMain().launch(tfUid.getText());
        new MainFrame("medprofs.main.title").start(MedProfsMain.class);
        closeWindow();
      }
    );
    loginSuccessActionMap.put(
      UserTypeHelpers.UT_PROFORG_AGENT, () -> {
        //new MedProfsMain().launch(tfUid.getText());
        new MainFrame("proforgs.main.title").start(ProfOrgAgentsMain.class);
        closeWindow();
      }
    );
  }

  public void onLogin(ActionEvent e) {
    ComboOptionData selectedUt = cmboUType.getSelectionModel().getSelectedItem();
    System.out.println("Selected user type: " + selectedUt.toString());

    Global.setResText(txtStatus, "login.loggingIn", Color.BLACK);

    String uid = tfUid.getText().trim();
    String pass = pfPass.getText();

    LoginHelpers.onLogin(
      selectedUt, uid, pass,
      () -> {
        Global.setResText(txtStatus, "login.loginSuccess", Color.GREEN);
        System.out.println("Login success!");
        loginSuccessActionMap.get(selectedUt.getCode()).run();
      },
      () -> {
        String loginFailedMsg = Global.AllRes.getString("login.loginFailed");
        Global.setText(txtStatus, loginFailedMsg, Color.RED);
      }
      );

  }
}
