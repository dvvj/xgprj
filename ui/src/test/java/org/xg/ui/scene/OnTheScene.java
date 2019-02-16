package org.xg.ui.scene;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OnTheScene extends Application {

  DoubleProperty fillVals = new SimpleDoubleProperty(255.0);

  Scene scene;

  ObservableList<Cursor> cursors = FXCollections.observableArrayList(
    Cursor.DEFAULT,
    Cursor.CROSSHAIR,
    Cursor.WAIT,
    Cursor.TEXT,
    Cursor.MOVE,
    Cursor.N_RESIZE,
    Cursor.NE_RESIZE,
    Cursor.E_RESIZE,
    Cursor.SE_RESIZE,
    Cursor.SW_RESIZE,
    Cursor.W_RESIZE,
    Cursor.NW_RESIZE,
    Cursor.NONE
  );

  public static void main(String[] args) {
    Application.launch(args);
  }

  private static Text createText() {
    Text txt = new Text();
    txt.getStyleClass().add("emphasized-text");
    return txt;
  }
  private static Label createLabel(String id) {
    Label lb = new Label();
    lb.setId(id);
    return lb;
  }
  private static void bindText(Text txt, ReadOnlyDoubleProperty v, String prefix) {
    txt.textProperty().bind(
      new SimpleStringProperty(prefix).concat(v.asString())
    );
  }
  private static void bindLabel(Label lbl, ReadOnlyDoubleProperty v, String prefix) {
    lbl.textProperty().bind(
      new SimpleStringProperty(prefix).concat(v.asString())
    );
  }
  @Override
  public void start(Stage primaryStage) throws Exception {
    Slider slider;
    ChoiceBox chb;
    Text txtSceneX;
    Text txtSceneY;
    Text txtSceneW;
    Text txtSceneH;
    Label lblStageX;
    Label lblStageY;
    Label lblStageW;
    Label lblStageH;

    ToggleGroup toggleGrp = new ToggleGroup();
    slider = new Slider(0, 255, 255);
    slider.setOrientation(Orientation.VERTICAL);

    chb = new ChoiceBox(cursors);
    HBox hbox = new HBox(slider, chb);

    hbox.setSpacing(10);
    txtSceneX = createText();
    txtSceneY = createText();
    txtSceneW = createText();
    txtSceneH = createText();
    String txtSceneHId = "sceneHeightText";
    txtSceneH.setId(txtSceneHId);

    Hyperlink hlink = new Hyperlink("lookup");
    hlink.setOnAction(e -> {
      System.out.println("scene: " + scene);
      Text txt = (Text)scene.lookup("#" + txtSceneHId);
      System.out.println(txt.getText());
    });

    RadioButton rb1 = new RadioButton("onTheScene.css");
    rb1.setSelected(true);
    rb1.setToggleGroup(toggleGrp);
    RadioButton rb2 = new RadioButton("changeOfScene.css");
    rb2.setToggleGroup(toggleGrp);

    lblStageX = createLabel("stageX");
    lblStageY = createLabel("stageY");
    lblStageW = createLabel("stageW");
    lblStageH = createLabel("stageH");

    FlowPane sceneRoot = new FlowPane(
      Orientation.VERTICAL,
      20, 10,
      hbox,
      txtSceneX, txtSceneY, txtSceneW, txtSceneH,
      hlink,
      rb1, rb2,
      lblStageX, lblStageY, lblStageW, lblStageH
    );

    sceneRoot.setPadding(new Insets(0, 20, 40, 0));
    sceneRoot.setColumnHalignment(HPos.LEFT);
    sceneRoot.setLayoutX(20);
    sceneRoot.setLayoutY(40);

    scene = new Scene(sceneRoot, 600, 250);
    scene.getStylesheets().add(
      getClass().getResource("/onTheScene.css").toExternalForm()
    );
    primaryStage.setScene(scene);

    chb.getSelectionModel().selectFirst();

    bindText(txtSceneX, scene.xProperty(), "Scene x: ");
    bindText(txtSceneY, scene.yProperty(), "Scene y: ");
    bindText(txtSceneX, scene.widthProperty(), "Scene width: ");
    bindText(txtSceneX, scene.heightProperty(), "Scene height: ");
    bindLabel(lblStageX, scene.getWindow().xProperty(), "Stage x: ");
    bindLabel(lblStageY, scene.getWindow().yProperty(), "Stage y: ");
    bindLabel(lblStageW, scene.getWindow().widthProperty(), "Stage width: ");
    bindLabel(lblStageH, scene.getWindow().heightProperty(), "Stage height: ");

    fillVals.bind(slider.valueProperty());

    fillVals.addListener((ov, oldv, newv) -> {
      double fillValue = fillVals.getValue() / 256.0;
      scene.setFill(new Color(fillValue, fillValue, fillValue, 1.0));
    });

    toggleGrp.selectedToggleProperty().addListener((ov, oldv, newv) -> {
      String rbText = ((RadioButton) toggleGrp.getSelectedToggle()).getText();
      scene.getStylesheets().clear();
      scene.getStylesheets().add(
        getClass().getResource("/" + rbText).toExternalForm()
      );;
    });

    scene.cursorProperty().bind(chb.getSelectionModel().selectedItemProperty());

    primaryStage.setTitle("On the scene");
    primaryStage.show();

    Text addedText = new Text(0, -30, "");
    addedText.setTextOrigin(VPos.TOP);
    addedText.setFill(Color.BLUE);
    addedText.setFont(Font.font("Sans Serif", FontWeight.BOLD, 16));
    addedText.setManaged(false);

    //bindText(addedText, scene.fillProperty());
    addedText.textProperty().bind(
      new SimpleStringProperty("Scene fill: ").concat(scene.fillProperty())
    );

    ((FlowPane)scene.getRoot()).getChildren().add(addedText);
  }
}
