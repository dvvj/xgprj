package org.xg.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.xg.auth.SvcHelpers;

public class UiLoginController {
  @FXML
  private TextField tfUid;

  @FXML
  private Button btnLogin;

  @FXML
  private PasswordField pfPass;

  private Stage stage;
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void onLogin(ActionEvent e) {
    System.out.println("login button pressed");
    String authUrl = "https://localhost:8443/webapi/auth/userPass";

    String token = SvcHelpers.authReq(
      authUrl,
      tfUid.getText(),
      pfPass.getText()
    );

    System.out.println("Response: " + token);
  }
}
