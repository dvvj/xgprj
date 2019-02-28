package org.xg.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.xg.ui.utils.Global;

public class PlOrTest extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader placeOrderLoader = new FXMLLoader(
      UiLoginController.class.getResource("/ui/PlaceOrder.fxml"),
      Global.AllRes
    );

    VBox placeOrder = placeOrderLoader.load();
    placeOrder.setPadding(
      new Insets(10)
    );

    stage.setScene(
      Global.sceneDefStyle(placeOrder)
    );
    stage.show();
  }
}
