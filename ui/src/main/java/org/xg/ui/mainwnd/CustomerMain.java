package org.xg.ui.mainwnd;

import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.xg.ui.*;
import org.xg.ui.utils.Global;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@ViewController(value = "/ui/CustomerMain.fxml")
public class CustomerMain {
  @FXML
  private HBox mainWnd;

  private CustomerMainRCtrl rightSideController;
  private ExistingOrdersCtrl orderController;
  private ProductTableController productTableController;

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
    return rightSide;
  }

  @PostConstruct
  public void launch() {
    try {

      mainWnd.getChildren().addAll(
        loadLeftSide(Global.getCurrUid(), Global.AllRes),
        loadRightSide("prod", Global.AllRes)
      );

      rightSideController.setBinding(
        productTableController.getSelectedProductDetail(),
        productTableController.getSelectedProductImageUrl(),
        productTableController.getSelectedProduct()
      );

//      Scene scene = Global.sceneDefStyle(root);
//
//      Stage mainStage = new Stage();
//      mainStage.setScene(scene);
//      mainStage.show();
    }
    catch (IOException ex) {
      throw new RuntimeException("Error launching main window!", ex);
    }
  }


}
