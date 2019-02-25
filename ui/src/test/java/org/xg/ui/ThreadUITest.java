package org.xg.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Random;

public class ThreadUITest extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    Pane root = new HBox();
    stage.setScene(new Scene(root, 300, 100));

    for (int i = 0; i < 5; i++) {
      final ProgressIndicator pi = new ProgressIndicator();

      root.getChildren().add(pi);

      new Thread() {

        // runnable for that thread
        public void run() {
          for (int i = 0; i < 20; i++) {
            try {
              // imitating work
              Thread.sleep(new Random().nextInt(1000));
            } catch (InterruptedException ex) {
              ex.printStackTrace();
            }
            final double progress = i*0.05;
            // update ProgressIndicator on FX thread
            Platform.runLater(new Runnable() {

              public void run() {
                pi.setProgress(progress);
              }
            });
          }
        }
      }.start();

//      Task<Void> task = new Task<>() {
//        @Override
//        public void run() {
//          for (int i = 0; i < 20; i++) {
//            try {
//              // imitating work
//              Thread.sleep(new Random().nextInt(1000));
//            } catch (InterruptedException ex) {
//              ex.printStackTrace();
//            }
//            final double progress = i * 0.05;
//            // update ProgressIndicator on FX thread
//            pi.setProgress(progress);
//          }
//
//        }
      };
    stage.show();
  }
}
