package org.xg.ui;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.xg.ui.model.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerMainRCtrl implements Initializable {

  @FXML
  private ImageView img;

  @FXML
  private TextArea txtDetails;

  public void bindDetails(ObservableValue<String> details) {
    txtDetails.textProperty().bind(details);
  }
                          @Override
  public void initialize(URL location, ResourceBundle resources) {
    img.setImage(
      new Image("file:///media/sf_vmshare/assets/4.png", 200, 400, true, true)
    );
  }
}
