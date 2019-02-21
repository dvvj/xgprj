package org.xg.ui;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LocationResourceBundleController {

  @FXML
  private Text txtTest;

  @FXML
  private URL location;

  @FXML
  private ResourceBundle resources;

  @FXML
  public void initialize() {
    String txt = String.format(
      "%s\n%s",
      location.toString(),
      resources.getBaseBundleName()
    );
    txtTest.setText(txt);
    //resourcesLabel.setText(resources.getBaseBundleName());
  }

}
