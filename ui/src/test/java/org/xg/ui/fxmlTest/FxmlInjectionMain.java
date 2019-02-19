package org.xg.ui.fxmlTest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FxmlInjectionMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(
      getClass().getResource("/FxmlInjection.fxml")
    );
    //loader.setResources();
    VBox vbox = loader.load();
    Scene scene = new Scene(vbox);
    primaryStage.setTitle("FXML injection example");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
