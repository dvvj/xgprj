package org.xg.ui.mainwnd;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.xg.ui.UiLoginController;
import org.xg.ui.utils.Global;

import java.io.IOException;
import java.net.URL;

public class MedProfsMain {
  public void launch(String userInfo) {
    try {
      FXMLLoader loader = new FXMLLoader(
        UiLoginController.class.getResource("/ui/MedProfsMain.fxml"),
        Global.AllRes
      );
      HBox root = loader.load();

      URL path = UiLoginController.class.getResource("/ui/CustomerTable.fxml");
      FXMLLoader customerLoader = new FXMLLoader(path, Global.AllRes);
      //productLoader.setLocation(path);
      TableView tv = customerLoader.load();
      root.getChildren().addAll(tv);

      Scene scene = Global.sceneDefStyle(root);

      Stage mainStage = new Stage();
      mainStage.setScene(scene);
      mainStage.show();

    }
    catch (IOException ex) {
      throw new RuntimeException("Error launching main window!", ex);
    }
  }
}
