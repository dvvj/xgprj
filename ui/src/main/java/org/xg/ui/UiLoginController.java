package org.xg.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.xg.ui.mainwnd.CustomerMain;
import org.xg.ui.mainwnd.MedProfsMain;
import org.xg.ui.model.UserType;
import org.xg.ui.model.UserTypeHelpers;
import org.xg.ui.utils.Global;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

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
  private ComboBox<UserType> cmboUType;

  private Map<Integer, UserType> userMap;

  private Map<Integer, Runnable> loginSuccessActionMap;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    userMap = UserTypeHelpers.createMap(resources);

    cmboUType.getItems().addAll(
      userMap.values()
    );

    cmboUType.setCellFactory(
      new Callback<ListView<UserType>, ListCell<UserType>>() {
        @Override
        public ListCell<UserType> call(ListView<UserType> param) {
          return new ListCell<UserType>() {
            @Override
            protected void updateItem(UserType item, boolean empty) {
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
      userMap.get(UserTypeHelpers.UT_CUSTOMER)
    );

    loginSuccessActionMap = new HashMap<>();
    loginSuccessActionMap.put(
      UserTypeHelpers.UT_CUSTOMER, () -> {
        new CustomerMain().launch(tfUid.getText());
        stage.close();
      }
    );
    loginSuccessActionMap.put(
      UserTypeHelpers.UT_MEDPROFS, () -> {
        new MedProfsMain().launch(tfUid.getText());
        stage.close();
      }
    );
  }

  private Stage stage;
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void onLogin(ActionEvent e) {
    UserType selectedUt = cmboUType.getSelectionModel().getSelectedItem();
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
    //System.out.println("login button pressed");
//    String authUrl = Global.getServerCfg().authURL(); //"https://localhost:8443/webapi/auth/userPass";
//
//    Task<AuthResp> authTask = Helpers.statusTaskJ(
//      () -> {
//        String uid = tfUid.getText().trim();
//        AuthResp resp = SvcHelpers.authReq(authUrl, uid, pfPass.getText());
//        if (resp.success()) {
//          Global.updateToken(uid, resp.token());
//        }
//        return resp;
//      },
//      resp -> {
//        if (resp != null && resp.success()) {
//          Global.setResText(txtStatus, "login.loginSuccess", Color.GREEN);
//          System.out.println("Login success!");
//          launchMain(tfUid.getText());
//          stage.close();
//        }
//        else {
//          String loginFailedMsg = Global.AllRes.getString("login.loginFailed");
//          Global.setText(txtStatus, loginFailedMsg, Color.RED);
//        }
//        return null;
//      },
//      30000
//    );
//
//    new Thread(authTask).start();

  }
}
