package org.xg.ui.comp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ErrorMessageCtrl {
  @FXML
  Label lblErrorMsg;

  public void setErrorMsg(String msg) {
    lblErrorMsg.setText(msg);
  }
}
