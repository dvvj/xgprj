package org.xg.ui.mainwnd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.xg.ui.UiLoginController;
import org.xg.ui.model.ProductTableHelper;
import org.xg.ui.utils.Global;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@ViewController(value = "/ui/MedProfsMain.fxml", title = "Material Design Example")
public class MedProfsMain implements Initializable {

  @FXML
  private HBox mainWnd;

  @FXML
  private TableView tblCustomers;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println("in init");
    tblCustomers.getColumns().addAll(
      ProductTableHelper.tableColumnResBundle(
        "customerTable.uid",
        Global.AllRes,
        "uid",
        100
      ),
      ProductTableHelper.tableColumnResBundle(
        "customerTable.name",
        Global.AllRes,
        "name",
        100
      ),
      ProductTableHelper.tableColumnResBundle(
        "customerTable.mobile",
        Global.AllRes,
        "mobile",
        100
      )
    );

    //Global.setSceneDefStyle(mainWnd);
  }

  @PostConstruct
  public void launch() {
    System.out.println("in launch");
//    try {
//      FXMLLoader loader = new FXMLLoader(
//        UiLoginController.class.getResource("/ui/MedProfsMain.fxml"),
//        Global.AllRes
//      );
//      HBox root = loader.load();

//      URL path = UiLoginController.class.getResource("/ui/CustomerTable.fxml");
//      FXMLLoader customerLoader = new FXMLLoader(path, Global.AllRes);
//      //productLoader.setLocation(path);
//      TableView tv = customerLoader.load();

//      JFXButton jfoenixButton = new JFXButton("JFoenix Button");

//      mainWnd.getChildren().addAll(tv);

//      Scene scene = Global.sceneDefStyle(mainWnd);
//
//      Stage mainStage = new Stage();
//      mainStage.setScene(scene);
//      mainStage.show();



//    }
//    catch (IOException ex) {
//      throw new RuntimeException("Error launching main window!", ex);
//    }
  }
}
