package org.xg.ui;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloEarthRiseScroll extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    String msg = "Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...Earthrise ...";

    Text txtRef = new Text(msg);
    txtRef.setLayoutY(100);
    txtRef.setTextOrigin(VPos.TOP);
    txtRef.setTextAlignment(TextAlignment.JUSTIFY);
    txtRef.setWrappingWidth(400);
    txtRef.setFill(Color.rgb(187, 195, 107));
    txtRef.setFont(Font.font("SansSerif", FontWeight.BOLD, 24));

//    Group txtGrp = new Group(txtRef);
//    txtGrp.setLayoutX(50);
//    txtGrp.setLayoutY(180);
//    txtGrp.setClip(new Rectangle(430, 85));

    ScrollPane scrPane = new ScrollPane();
    scrPane.setLayoutX(50);
    scrPane.setLayoutY(180);
    scrPane.setPrefWidth(400);
    scrPane.setPrefHeight(85);
    scrPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrPane.setPannable(true);
    scrPane.setContent(txtRef);
    scrPane.setStyle("-fx-background-color: transparent;");

    ImageView iv = new ImageView(
      new Image("http://projavafx.com/images/earthrise.jpg")
    );

    Group root = new Group(iv, scrPane);
    Scene scene = new Scene(root, 516, 387);
    stage.setScene(scene);
    stage.show();

    TranslateTransition transTransition = new TranslateTransition(new Duration(75000), txtRef);
    transTransition.setToY(-820);
    transTransition.setInterpolator(Interpolator.LINEAR);
    transTransition.setCycleCount(Timeline.INDEFINITE);

    transTransition.play();
  }
}
