package org.xg.ui.comp;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class WeixinPayCtrl {

  @FXML
  private ImageView qrCode;

  public void setQrCode(byte[] codeData) {
    ByteArrayInputStream in = new ByteArrayInputStream(codeData);
    qrCode.setImage(new Image(in));
    //in.close();
  }

}
