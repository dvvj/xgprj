package org.xg.ui.fxmlTest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StageCoachFxmlMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/StageCoach.fxml")
    );

    Group rootGrp = loader.load();

    StageCoachController controller = loader.getController();

    controller.setStage(primaryStage);

    Scene scene = new Scene(rootGrp, 350, 300);
    scene.setFill(Color.TRANSPARENT);

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
