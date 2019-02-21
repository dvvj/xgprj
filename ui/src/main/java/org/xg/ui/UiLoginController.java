package org.xg.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.xg.auth.AuthResp;
import org.xg.auth.SvcHelpers;

import java.io.IOException;
import java.util.ResourceBundle;

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

  private void launchMain(String fxmlMain) {
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource(fxmlMain)
      );
      loader.setResources(
        ResourceBundle.getBundle("ui.CustomerMain")
      );
      HBox root = loader.load();
      CustomerMainController controller = loader.getController();

      Scene scene = new Scene(root);
      Stage mainStage = new Stage();
      mainStage.setScene(scene);
      mainStage.show();
    }
    catch (IOException ex) {
      throw new RuntimeException("Error launching main window!", ex);
    }
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
      launchMain("/ui/CustomerMain.fxml");
      stage.close();
    }
    else {
      txtStatus.setText("failed");
      txtStatus.setStroke(Color.RED);
    }
  }
}
