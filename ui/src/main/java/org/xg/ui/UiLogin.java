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
    ResourceBundle resBundle = Global.getBundle("ui.UiLogin");
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/ui/UiLogin.fxml"),
      resBundle
    );
    HBox root = loader.load();
    UiLoginController controller = loader.getController();
    controller.setStage(stage);

//    Font font = Font.loadFont(
//      getClass().getResourceAsStream("/NotoSansCJK-Medium.ttc"), 18
//    );
//    Logging.debug("Font loaded: [%s]", font.getFamily());

    Scene scene = Global.sceneDefStyle(root);
    scene.setFill(Color.TRANSPARENT);
//    scene.getStylesheets().add(
//      getClass().getResource("/default.css").toExternalForm()
//    );

    stage.setScene(scene);
    stage.setTitle("title");
    stage.show();
//    Label lblUid = new Label("用户 ID");
//    Label lblUType = new Label("用户类型");
//    Label lblPass = new Label("登录密码");
//    VBox vboxLabels = new VBox(lblUid, lblUType, lblPass);
//    vboxLabels.setAlignment(Pos.BASELINE_RIGHT);
//    vboxLabels.setLayoutX(20);
//    vboxLabels.setLayoutY(20);
//    vboxLabels.setSpacing(10);
//
//    Text txtUid = new Text();
//    Text txtUType = new Text("todo");
//    PasswordField pass = new PasswordField();
//    pass.setPromptText("您的密码");
//
//    VBox vboxInputs = new VBox(txtUid, txtUType, pass);
//    vboxInputs.setSpacing(10);
//
//    HBox hboxAll = new HBox(vboxLabels, vboxInputs);
//    hboxAll.setSpacing(20);
//    hboxAll.setPadding(new Insets(40));
//    hboxAll.setAlignment(Pos.CENTER);
//
//
//    Scene scene = new Scene(hboxAll);
//    scene.setFill(Color.TRANSPARENT);
//    scene.getStylesheets().add(
//      getClass().getResource("/default.css").toExternalForm()
//    );
//
//    stage.setScene(scene);
//
//    stage.show();
  }
}
