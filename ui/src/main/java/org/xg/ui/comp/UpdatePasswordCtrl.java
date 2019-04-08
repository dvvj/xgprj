package org.xg.ui.comp;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.xg.ui.UiLoginController;
import org.xg.ui.utils.Global;

import java.net.URL;

public class UpdatePasswordCtrl {
  @FXML
  JFXButton btnUpdatePass;

  public static void load2Tab(Pane parent) throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/UpdatePassword.fxml");
    FXMLLoader updatePassLoader = new FXMLLoader(path, Global.AllRes);
    VBox updatePass = updatePassLoader.load();

    parent.getChildren().addAll(updatePass);
  }
}
