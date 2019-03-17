package org.xg.ui;

import io.datafx.controller.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.xg.ui.mainwnd.CustomerMain;
import org.xg.ui.mainwnd.MainFrame;
import org.xg.ui.mainwnd.MedProfsMain;
import org.xg.ui.model.UserType;
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
  private ComboBox<UserType> cmboUType;

  @FXML
  private void closeWindow() {
    final Stage stage = (Stage)cmboUType.getScene().getWindow();
    stage.close();
  }

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

  }
}
