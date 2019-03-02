package org.xg.ui;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.xg.auth.AuthResp;
import org.xg.auth.SvcHelpers;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;

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

  @FXML
  private CheckBox chbRemember;

  private Stage stage;
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  private Node loadLeftSide(String userInfo, ResourceBundle resBundle) throws IOException {

    VBox leftSide = new VBox();
    leftSide.setPadding(
      new Insets(10)
    );
    leftSide.setSpacing(10);

    HBox greetings = new HBox();
    greetings.setSpacing(10);
    Text txtWelcome = new Text();
    txtWelcome.setText(resBundle.getString("greeting.welcome"));
    Text txtUserInfo = new Text();
    txtUserInfo.setText(userInfo);
    txtUserInfo.setStroke(Color.GREEN);
    greetings.getChildren().addAll(txtWelcome, txtUserInfo);

    URL path = UiLoginController.class.getResource("/ui/ProductTable.fxml");
    FXMLLoader productLoader = new FXMLLoader(path, resBundle);
    //productLoader.setLocation(path);
    TableView tv = productLoader.load();
    productTableController = productLoader.getController();

    URL pathOrders = UiLoginController.class.getResource("/ui/ExistingOrders.fxml");
    FXMLLoader orderLoader = new FXMLLoader(pathOrders, resBundle);
    TableView orderTable = orderLoader.load();
    orderController = orderLoader.getController();

    leftSide.getChildren().addAll(greetings, tv, orderTable);
    return leftSide;

  }

  private ExistingOrdersCtrl orderController;

  private ProductTableController productTableController;

  private Node loadRightSide(String productName, ResourceBundle resBundle) throws IOException {

    FXMLLoader rightSideLoader = new FXMLLoader(
      UiLoginController.class.getResource("/ui/CustomerMainR.fxml"),
      resBundle
    );
    VBox rightSide = rightSideLoader.load();
    rightSide.setPadding(
      new Insets(20)
    );
    rightSideController = rightSideLoader.getController();
//    FXMLLoader placeOrderLoader = new FXMLLoader(
//      UiLoginController.class.getResource("/ui/PlaceOrder.fxml"),
//      resBundle
//    );
//
//    VBox placeOrder = placeOrderLoader.load();
//
//    rightSide.getChildren().add(placeOrder);

    return rightSide;
  }
  private CustomerMainRCtrl rightSideController;

  private void launchMain(String userInfo) {
    try {

      FXMLLoader loader = new FXMLLoader(
        UiLoginController.class.getResource("/ui/CustomerMain.fxml"),
        Global.AllRes
      );

      HBox root = loader.load();
      CustomerMainController controller = loader.getController();

      root.getChildren().addAll(
        loadLeftSide(userInfo, Global.AllRes),
        loadRightSide("prod", Global.AllRes)
      );

      rightSideController.setBinding(
        productTableController.getSelectedProductDetail(),
        productTableController.getSelectedProductImageUrl(),
        productTableController.getSelectedProduct()
      );

      Scene scene = Global.sceneDefStyle(root);

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

    Global.setText(txtStatus, "login.loggingIn", Color.BLACK);

//    AuthResp resp = SvcHelpers.authReq(
//      authUrl,
//      tfUid.getText(),
//      pfPass.getText()
//    );

    Task<AuthResp> authTask = Helpers.statusTaskJ(
      () -> {
        AuthResp resp = SvcHelpers.authReq(authUrl, tfUid.getText(), pfPass.getText());
        if (resp.success()) {
          //System.out.println(resp.token());
          Global.updateToken(resp.token());
        }
        return resp;
      },
      "done",
      resp -> {
        if (resp.success()) {
          Global.setText(txtStatus, "login.loginSuccess", Color.GREEN);
          launchMain(tfUid.getText());
          stage.close();
        }
        else {
          Global.setText(txtStatus, "login.loginFailed", Color.RED);
        }
        return null;
      }
    );

    new Thread(authTask).start();

//    if (resp.success()) {
//      //System.out.println(resp.token());
//      Global.updateToken(resp.token());
//      txtStatus.setText("success");
//      txtStatus.setStroke(Color.GREEN);
//      launchMain(tfUid.getText());
//      stage.close();
//    }
//    else {
//      txtStatus.setText("failed");
//      txtStatus.setStroke(Color.RED);
//    }
  }
}
