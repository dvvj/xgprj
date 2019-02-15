package org.xg.ui;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloEarthRise extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String msg = "Earthrise ...";

        Text txtRef = new Text(msg);
        txtRef.setLayoutY(100);
        txtRef.setTextOrigin(VPos.TOP);
        txtRef.setTextAlignment(TextAlignment.JUSTIFY);
        txtRef.setWrappingWidth(400);
        txtRef.setFill(Color.rgb(187, 195, 107));
        txtRef.setFont(Font.font("SansSerif", FontWeight.BOLD, 24));

        TranslateTransition transTransition = new TranslateTransition(new Duration(75000), txtRef);
        transTransition.setToY(-820);
        transTransition.setInterpolator(Interpolator.LINEAR);
        transTransition.setCycleCount(Timeline.INDEFINITE);

        Group root = new Group(txtRef);
        Scene scene = new Scene(root, 516, 387);
        stage.setScene(scene);
        stage.show();

        transTransition.play();
    }
}
