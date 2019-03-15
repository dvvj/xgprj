package org.xg.ui;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xg.ui.utils.Global;

public class JFoeniXTest extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {

    VBox vb = new VBox();
    vb.setPadding(
      new Insets(10)
    );

    JFXButton button = new JFXButton("Raised Button".toUpperCase());
    button.getStyleClass().add("button-raised");

    JFXCheckBox checkBox = new JFXCheckBox("JFX CheckBox");
    checkBox.getStyleClass().add("custom-jfx-check-box");

    JFXComboBox<Label> jfxCombo = new JFXComboBox<Label>();

    jfxCombo.getItems().add(new Label("Java 1.8"));
    jfxCombo.getItems().add(new Label("Java 1.7"));
    jfxCombo.getItems().add(new Label("Java 1.6"));
    jfxCombo.getItems().add(new Label("Java 1.5"));
    jfxCombo.setPromptText("Select Java Version");

//    JFXHamburger h1 = new JFXHamburger();
//    HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(h1);
//    burgerTask.setRate(-1);
//    h1.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
//      burgerTask.setRate(burgerTask.getRate()*-1);
//      burgerTask.play();
//    });

    JFXTextField field = new JFXTextField();
    field.setLabelFloat(true);
    field.setPromptText("Floating prompt");

    JFXTextField validationField = new JFXTextField();
    validationField.setPromptText("With Validation..");
    RequiredFieldValidator validator = new RequiredFieldValidator();
    validator.setMessage("Input Required");
//    validator.setAwsomeIcon(new Icon(AwesomeIcon.WARNING,"2em",";","error"));
    validationField.getValidators().add(validator);
    validationField.focusedProperty().addListener((o,oldVal,newVal)->{
      if(!newVal) validationField.validate();
    });

    JFXProgressBar jfxBar = new JFXProgressBar();
    jfxBar.setPrefWidth(500);
    JFXProgressBar jfxBarInf = new JFXProgressBar();
    jfxBarInf.setPrefWidth(500);
    jfxBarInf.setProgress(-1.0f);
//    Timeline timeline = new Timeline(
//      new KeyFrame(Duration.ZERO, new KeyValue(bar.progressProperty(), 0), new KeyValue(jfxBar.progressProperty(), 0)),
//      new KeyFrame(Duration.seconds(2), new KeyValue(bar.progressProperty(), 1), new KeyValue(jfxBar.progressProperty(), 1)));
//    timeline.setCycleCount(Timeline.INDEFINITE);
//    timeline.play();

    final ToggleGroup group = new ToggleGroup();

    JFXRadioButton javaRadio = new JFXRadioButton("JavaFX");
    javaRadio.setPadding(new Insets(10));
    javaRadio.setToggleGroup(group);
    JFXRadioButton jfxRadio = new JFXRadioButton("JFoenix");
    jfxRadio.setPadding(new Insets(10));
    jfxRadio.setToggleGroup(group);

    JFXSlider hor_left = new JFXSlider();
    hor_left.setMinWidth(500);
    JFXSlider hor_right = new JFXSlider();
    hor_left.setMinWidth(500);
    hor_left.setIndicatorPosition(JFXSlider.IndicatorPosition.RIGHT);
//    JFXSlider ver_left = new JFXSlider();
//    ver_left.setMinHeight(500);
//    ver_left.setOrientation(Orientation.VERTICAL);
//    JFXSlider ver_right = new JFXSlider();
//    ver_right.setMinHeight(500);
//    ver_right.setOrientation(Orientation.VERTICAL);
//    ver_right.setIndicatorPosition(JFXSlider.IndicatorPosition.RIGHT);

    JFXSpinner spinner = new JFXSpinner();

    JFXTabPane tabPane = new JFXTabPane();
    tabPane.setPrefSize(300, 200);
    Tab tab = new Tab();
    tab.setText("Tab 1");
    tab.setContent(new Label("Content"));
    tabPane.getTabs().add(tab);
    tab = new Tab();
    tab.setText("Tab 2");
    tab.setContent(new Label("Content"));
    tabPane.getTabs().add(tab);

    JFXToggleButton toggleButton = new JFXToggleButton();

    JFXToggleNode node = new JFXToggleNode();
    Label lbl = new Label();
    lbl.setText("toggle");
    node.setGraphic(lbl);

    vb.getChildren().addAll(
      button,
      checkBox,
      jfxCombo,
      field, validationField,
      jfxBar,
      javaRadio, jfxRadio,
      hor_left, hor_right,
      spinner,
      tabPane,
      toggleButton
    );

    stage.setScene(
      Global.sceneDefStyle(vb)
    );
    stage.show();
  }
}
