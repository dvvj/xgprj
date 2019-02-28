package org.xg.ui;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
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

  private Label lblUrl = new Label();
  //private StringProperty imgUrlProp = new SimpleStringProperty();

  public void setBinding(
    ObservableValue<String> details,
    ObservableValue<String> imageUrl
  ) {
    txtDetails.textProperty().bind(details);
    lblUrl.textProperty().bind(imageUrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    lblUrl.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.equals(oldValue)) {
          System.out.println("updating image");
          //Image image = new Image(lblUrl.getText(), 200, 400, true, true);
          //Image image = new Image()
//          img.setImage(image);
//
//          System.out.println(
//            String.format("setting image to [%s], progress [%.3f]", newValue, image.getProgress())
//          );
//          image.progressProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//              if (newValue.doubleValue() >= 1.0) {
//                Platform.runLater(new Runnable() {
//                  @Override
//                  public void run() {
//                    img.setImage(image);
//                  }
//                });
//              }
//            }
//          });

        }
      }
    });
    img.setImage(
      new Image("file:///media/sf_vmshare/assets/4.png", 200, 400, true, true)
    );
  }
}
