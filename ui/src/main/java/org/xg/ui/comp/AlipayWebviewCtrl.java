package org.xg.ui.comp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import org.xg.pay.AlipayCfg;
import org.xg.pay.AlipayHelpers;
import org.xg.ui.model.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class AlipayWebviewCtrl implements Initializable {
  @FXML
  private WebView wvAlipay;

  private StringProperty amount = new SimpleStringProperty();
  private double unitPrice;

  public void setAmountAndSendReq(Product product, ObservableValue<String> qty) {
    amount.bind(qty);
    this.unitPrice = product.getPrice0();

    String privateKeyPath = "/home/devvj/alipay-keys/rsa_private_key.raw";
    AlipayCfg cfg = AlipayHelpers.testLocalCfg(privateKeyPath);
    Double total = unitPrice * Double.parseDouble(amount.getValue());
    String pageContent = AlipayHelpers.test1RandTraceNo(cfg, product.getName(), total);
    wvAlipay.getEngine().loadContent(pageContent);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    wvAlipay.getEngine().setUserStyleSheetLocation(
      getClass().getResource("/default.css").toExternalForm()
    );
  }
}
