package org.xg.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerMainRCtrl implements Initializable {

  @FXML
  private ImageView img;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    img.setImage(
      new Image("file:///home/devvj/tmp/png/100-sanitizer.png", 300, 400, true, true)
    );
  }
}
