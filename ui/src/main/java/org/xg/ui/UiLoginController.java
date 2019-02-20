package org.xg.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.xg.auth.AuthResp;
import org.xg.auth.SvcHelpers;

public class UiLoginController {
  @FXML
  private TextField tfUid;

  @FXML
  private Button btnLogin;

  @FXML
  private PasswordField pfPass;

  @FXML
  private Text txtStatus;

  private Stage stage;
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void onLogin(ActionEvent e) {
    //System.out.println("login button pressed");
    String authUrl = "https://localhost:8443/webapi/auth/userPass";

    AuthResp resp = SvcHelpers.authReq(
      authUrl,
      tfUid.getText(),
      pfPass.getText()
    );

    if (resp.success()) {
      System.out.println(resp.token());
      txtStatus.setText("success");
      txtStatus.setStroke(Color.GREEN);
    }
    else {
      txtStatus.setText("failed");
      txtStatus.setStroke(Color.RED);
    }
  }
}
