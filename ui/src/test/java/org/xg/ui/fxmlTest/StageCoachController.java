package org.xg.ui.fxmlTest;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StageCoachController {

  @FXML
  private Rectangle blue;

  @FXML
  private Text textStageX;
  @FXML
  private Text textStageY;
  @FXML
  private Text textStageH;
  @FXML
  private Text textStageW;

  @FXML
  private CheckBox checkBoxResizable;
  @FXML
  private CheckBox checkBoxFullScreen;

  @FXML
  private HBox titleBox;
  @FXML
  private Label titleLabel;
  @FXML
  private TextField titleTextField;

  private Stage stage;
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
