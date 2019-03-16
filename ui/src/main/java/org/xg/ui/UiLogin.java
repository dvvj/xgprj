package org.xg.ui;

import com.sun.javafx.tk.FontLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.xg.log.Logging;
import org.xg.ui.mainwnd.MainFrame;
import org.xg.ui.mainwnd.MedProfsMain;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Utf8ResBundleCtrl;

import java.util.Locale;
import java.util.ResourceBundle;

public class UiLogin extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    new MainFrame("login.main.title").start(stage, UiLoginController.class);
//    FXMLLoader loader = new FXMLLoader(
//      getClass().getResource("/ui/UiLogin.fxml"),
//      Global.AllRes
//    );
//    HBox root = loader.load();
//    UiLoginController controller = loader.getController();
//    controller.setStage(stage);
//
////    Font font = Font.loadFont(
////      getClass().getResourceAsStream("/NotoSansCJK-Medium.ttc"), 18
////    );
////    Logging.debug("Font loaded: [%s]", font.getFamily());
//
//    Scene scene = Global.sceneDefStyle(root);
//    scene.setFill(Color.TRANSPARENT);
////    scene.getStylesheets().add(
////      getClass().getResource("/default.css").toExternalForm()
////    );
//
//    stage.setResizable(false);
//    stage.setScene(scene);
//    stage.setTitle(Global.AllRes.getString("login.windowTitle"));
//    stage.show();

  }
}
