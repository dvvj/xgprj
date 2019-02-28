package org.xg.ui.comp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import org.xg.pay.AlipayCfg;
import org.xg.pay.AlipayHelpers;

import java.net.URL;
import java.util.ResourceBundle;

public class AlipayWebviewCtrl implements Initializable {
  @FXML
  private WebView wvAlipay;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    wvAlipay.getEngine().setUserStyleSheetLocation(
      getClass().getResource("/default.css").toExternalForm()
    );

    String privateKeyPath = "/home/devvj/alipay-keys/rsa_private_key.raw";
    AlipayCfg cfg = AlipayHelpers.testLocalCfg(privateKeyPath);
    String pageContent = AlipayHelpers.test1RandTraceNo(cfg);
    wvAlipay.getEngine().loadContent(pageContent);
  }
}
