package org.xg.ui.stage;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class StageCoachMain extends Application {

  private StringProperty title = new SimpleStringProperty();

  Text txtStageX;
  Text txtStageY;

  Text txtStageH;
  Text txtStageW;

  Text txtStageF;

  CheckBox chbResizable;
  CheckBox chbFullScreen;

  double dragAnchorX;
  double dragAnchorY;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    StageStyle stageStyle = StageStyle.DECORATED;

    List<String> unnamedParams = getParameters().getUnnamed();
    if (unnamedParams.size() > 0) {
      String stageStyleParam = unnamedParams.get(0);
      if (stageStyleParam.equalsIgnoreCase("transparent")) {
        stageStyle = StageStyle.TRANSPARENT;
      }
      else if (stageStyleParam.equalsIgnoreCase("undecorated")) {
        stageStyle = StageStyle.UNDECORATED;
      }
      else if (stageStyleParam.equalsIgnoreCase("utility")) {
        stageStyle = StageStyle.UTILITY;
      }
      else {
        System.out.println("Ignored unnamed parameter: " + stageStyleParam);
      }
    }

    final Stage stageRef = stage;

    Group rootGrp;
    TextField titleTf;

    Button toBackButton = new Button("toBack()");
    toBackButton.setOnAction(e -> stageRef.toBack());

    Button toFrontButton = new Button("toFront()");
    toFrontButton.setOnAction(e -> stageRef.toFront());

    Button closeButton = new Button("close()");
    closeButton.setOnAction(e -> stageRef.close());

    Rectangle blue = new Rectangle(250, 350, Color.SKYBLUE);
    blue.setArcWidth(50);
    blue.setArcHeight(50);

    txtStageX = newTextPosTop();
    txtStageY = newTextPosTop();
    txtStageH = newTextPosTop();
    txtStageW = newTextPosTop();
    txtStageF = newTextPosTop();

    chbResizable = new CheckBox("resizable");
    chbResizable.setDisable(
      stageStyle == StageStyle.TRANSPARENT || stageStyle == StageStyle.UNDECORATED
    );

    chbFullScreen = new CheckBox("full screen");
    titleTf = new TextField("Stage Coach");

    Label titleLbl = new Label("title");
    HBox titleBox = new HBox(titleLbl, titleTf);
    VBox contentBox = new VBox(
      txtStageX, txtStageY, txtStageW, txtStageH, txtStageF,
      chbResizable, chbFullScreen,
      titleBox, toBackButton, toFrontButton, closeButton
    );
    contentBox.setLayoutX(30);
    contentBox.setLayoutY(20);
    contentBox.setSpacing(10);

    rootGrp = new Group(blue, contentBox);

    Scene scene = new Scene(rootGrp, 270, 370);
    scene.setFill(Color.TRANSPARENT);

    // when mouse button is pressed, save the inital position of screen
    rootGrp.setOnMousePressed(me -> {
      dragAnchorX = me.getScreenX() - stageRef.getX();
      dragAnchorY = me.getScreenY() - stageRef.getY();
    });

    // when screen is dragged, translate it accordingly
    rootGrp.setOnMouseDragged(me -> {
      stageRef.setX(me.getScreenX() - dragAnchorX);
      stageRef.setY(me.getScreenY() - dragAnchorY);
    });

    bindText(txtStageX,"x: ", stageRef.xProperty());
    bindText(txtStageY,"y: ", stageRef.yProperty());
    bindText(txtStageH,"height: ", stageRef.heightProperty());
    bindText(txtStageW,"width: ", stageRef.widthProperty());
    bindText(txtStageF,"focused: ", stageRef.focusedProperty());

    stage.setResizable(true);

    chbResizable.selectedProperty().bindBidirectional(stage.resizableProperty());
    chbFullScreen.selectedProperty().addListener(
      (ov, oldValue, newValue) -> {
        stageRef.setFullScreen(chbFullScreen.selectedProperty().getValue());
      }
    );

    title.bind(titleTf.textProperty());

    stage.setScene(scene);
    stage.titleProperty().bind(title);
    stage.initStyle(stageStyle);
    stage.setOnCloseRequest(we -> {
      System.out.println("Stage is closing");
    });

    stage.show();
    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    stage.setX(
      (primScreenBounds.getWidth() - stage.getWidth()) / 2
    );
    stage.setY(
      (primScreenBounds.getHeight() - stage.getHeight()) / 4
    );
  }

  private static void bindText(Text txt, String indTxt, ReadOnlyDoubleProperty prop) {
    txt.textProperty().bind(
      new SimpleStringProperty(indTxt).concat(prop.asString())
    );
  }

  private static void bindText(Text txt, String indTxt, ReadOnlyBooleanProperty prop) {
    txt.textProperty().bind(
      new SimpleStringProperty(indTxt).concat(prop.asString())
    );
  }

  private static Text newTextPosTop() {
    Text txt = new Text();
    txt.setTextOrigin(VPos.TOP);
    return txt;
  }
}
