package org.xg.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.xg.auth.AuthResp;
import org.xg.auth.SvcHelpers;

import java.io.IOException;
import java.net.URL;
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

  private void launchMain() {
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/ui/CustomerMain.fxml")
      );
      loader.setResources(
        ResourceBundle.getBundle("ui.CustomerMain")
      );
      HBox root = loader.load();
      CustomerMainController controller = loader.getController();

      URL path = getClass().getResource("/ui/ProductTable.fxml");
      FXMLLoader productLoader = new FXMLLoader(path);
      //productLoader.setLocation(path);
      TableView tv = productLoader.load();
      ProductTableController productTableController = productLoader.getController();

      FXMLLoader productDetailLoader = new FXMLLoader(
        getClass().getResource("/ui/ProductDetail.fxml")
      );
      VBox detailBox = productDetailLoader.load();
      root.getChildren().addAll(tv, detailBox);

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
      launchMain();
      stage.close();
    }
    else {
      txtStatus.setText("failed");
      txtStatus.setStroke(Color.RED);
    }
  }
}
