package org.xg.ui.AudioConfEx;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AudioConfMain extends Application {

  private AudioConfModel acModel = new AudioConfModel();

  private Text txtDb;
  private Slider slider;
  private CheckBox mutedCheckBox;
  private ChoiceBox<String> genreChoiceBox;
  private Color color = Color.color(0.66, 0.67, 0.69);

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    Text title = new Text(65, 12, "Audio Config");

    title.setTextOrigin(VPos.TOP);
    title.setFill(Color.WHITE);
    title.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));

    Font fontB18 = Font.font("SansSerif", FontWeight.BOLD, 18);
    Color color = Color.web("#131021");
    Text txtDb = new Text();
    txtDb.setLayoutX(18);
    txtDb.setLayoutY(69);
    txtDb.setTextOrigin(VPos.TOP);
    txtDb.setFill(color);
    txtDb.setFont(fontB18);

    Text txtMuted = new Text(18, 113, "Muted");
    txtMuted.setTextOrigin(VPos.TOP);
    txtMuted.setFont(fontB18);
    txtMuted.setFill(color);

    Text txtGenre = new Text(18, 154, "Genre");
    txtGenre.setTextOrigin(VPos.TOP);
    txtGenre.setFill(color);
    txtGenre.setFont(fontB18);

    slider = new Slider();
    slider.setLayoutX(135);
    slider.setLayoutY(69);
    slider.setPrefWidth(162);
    slider.setMin(acModel.minDecibels);
    slider.setMax(acModel.maxDecibels);

    mutedCheckBox = new CheckBox();
    mutedCheckBox.setLayoutX(280);
    mutedCheckBox.setLayoutY(113);

    genreChoiceBox = new ChoiceBox<>();
    genreChoiceBox.setLayoutX(204);
    genreChoiceBox.setLayoutY(154);
    genreChoiceBox.setPrefWidth(93);
    genreChoiceBox.setItems(acModel.genres);
    Stop[] stops = new Stop[] {
      new Stop(0, Color.web("0xAEBBCC")),
      new Stop(1, Color.web("0x6D84A3"))
    };

    LinearGradient linGrad = new LinearGradient(
      0, 0, 0, 1, true,
      CycleMethod.NO_CYCLE, stops
    );

    Rectangle rect = new Rectangle(0, 0, 320, 45);
    rect.setFill(linGrad);
    Rectangle rect2 = new Rectangle(0, 43, 320, 300);
    rect2.setFill(Color.rgb(199, 206, 213));
    Rectangle rect3 = new Rectangle(8, 54, 300, 130);
    rect3.setArcHeight(20);
    rect3.setArcWidth(20);
    rect3.setFill(Color.WHITE);
    rect3.setStroke(color);

    Line l1 = new Line(9, 97, 309, 97);
    l1.setStroke(color);
    Line l2 = new Line(9, 141, 309, 141);
    l2.setFill(color);

    Group grp = new Group(rect, title, rect2, rect3,
      txtDb,
      slider,
      l1,
      txtMuted,
      mutedCheckBox,
      l2,
      txtGenre,
      genreChoiceBox
    );

    Scene scene = new Scene(grp, 320, 343);

    txtDb.textProperty().bind(acModel.selectedDBs.asString().concat(" dB"));
    slider.valueProperty().bindBidirectional(acModel.selectedDBs);
    slider.disableProperty().bind(acModel.muted);
    mutedCheckBox.selectedProperty().bindBidirectional(acModel.muted);
    acModel.genreSelection = genreChoiceBox.getSelectionModel();
    acModel.addListener2GenreSelection();
    acModel.genreSelection.selectFirst();

    stage.setScene(scene);
    stage.setTitle("Audio Conf");
    stage.show();
  }
}
