package org.xg.ui.mainwnd;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.ViewController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

  @FXML
  private Text txtUserName;

  @FXML
  private StackPane optionsBurger;

  private CustomerMainRCtrl rightSideController;
  private ExistingOrdersCtrl orderController;
  private ProductTableController productTableController;

  @FXML
  private VBox leftSide;
  private Node loadLeftSide(String userInfo, ResourceBundle resBundle) throws IOException {

//    VBox leftSide = new VBox();
//    leftSide.setPadding(
//      new Insets(5)
//    );
//    leftSide.setSpacing(5);

//    HBox greetings = new HBox();
//    greetings.setSpacing(10);
//    Text txtWelcome = new Text();
//    txtWelcome.setText(resBundle.getString("greeting.welcome"));
//    Text txtUserInfo = new Text();
//    txtUserInfo.setText(userInfo);
//    txtUserInfo.setStroke(Color.GREEN);
//    greetings.getChildren().addAll(txtWelcome, txtUserInfo);
    txtUserName.setText(Global.getCurrUid()); // todo use name instead
    txtUserName.setFill(Color.WHEAT);

    URL path = UiLoginController.class.getResource("/ui/ProductTable.fxml");
    FXMLLoader productLoader = new FXMLLoader(path, resBundle);
    //productLoader.setLocation(path);
    JFXTreeTableView tv = productLoader.load();
    productTableController = productLoader.getController();

    URL pathOrders = UiLoginController.class.getResource("/ui/ExistingOrders.fxml");
    FXMLLoader orderLoader = new FXMLLoader(pathOrders, resBundle);
    VBox orderCtrl = orderLoader.load();
    orderController = orderLoader.getController();

    leftSide.getChildren().addAll(tv, orderCtrl);
    return leftSide;

  }

  private Node loadRightSide(String productName, ResourceBundle resBundle) throws IOException {

    FXMLLoader rightSideLoader = new FXMLLoader(
      UiLoginController.class.getResource("/ui/CustomerMainR.fxml"),
      resBundle
    );
    VBox rightSide = rightSideLoader.load();
//    rightSide.setPadding(
//      new Insets(20)
//    );
    rightSideController = rightSideLoader.getController();
    return rightSide;
  }

  private JFXPopup toolbarPopup;

  @PostConstruct
  public void launch() {
    try {

      loadLeftSide(Global.getCurrUid(), Global.AllRes);
      mainWnd.getChildren().addAll(
        loadRightSide("prod", Global.AllRes)
      );

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SettingsPopup.fxml"), Global.AllRes);
      loader.setController(new SettingsController());
      toolbarPopup = new JFXPopup(loader.load());

      optionsBurger.setOnMouseClicked(e -> {
        toolbarPopup.show(
          optionsBurger,
          JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT,
          0, 0
        );
      });

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

  public static final class SettingsController {
    @FXML
    private JFXListView<?> toolbarPopupList;

    // close application
    @FXML
    private void submit() {
      if (toolbarPopupList.getSelectionModel().getSelectedIndex() == 1) {
        Platform.exit();
      }
    }
  }
}
