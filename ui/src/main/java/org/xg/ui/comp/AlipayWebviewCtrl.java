package org.xg.ui.comp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import org.xg.auth.SvcHelpers;
import org.xg.pay.AlipayCfg;
import org.xg.pay.AlipayHelpers;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiModels.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class AlipayWebviewCtrl implements Initializable {
  @FXML
  private WebView wvAlipay;

  private static final String htmlTemplate = "<!doctype html>\n" +
    "\n" +
    "<html lang=\"en\">\n" +
    "<head>\n" +
    "  <meta charset=\"utf-8\">\n" +
    "  <style>\n" +
    "     html { font-family: 'Noto Sans CJK SC Medium'; }\n" +
    "  </style>" +
    "\n" +
    "  <title>Alipay</title>\n" +
    "</head>\n" +
    "\n" +
    "<body>\n" +
    "  %s\n" +
    "</body>\n" +
    "</html>";

  private StringProperty amount = new SimpleStringProperty();
  private double unitPrice;
  private Long _orderId;

  public void setOrderInfoAndSendReq(Long orderId, Product product, ObservableValue<String> qty) {
    amount.bind(qty);
    _orderId = orderId;
    this.unitPrice = product.getActualPrice();

    //String privateKeyPath = "/home/devvj/alipay-keys/rsa_private_key.raw";
    AlipayCfg cfg = AlipayHelpers.testLocalCfg();
    Double total = unitPrice * Double.parseDouble(amount.getValue());
    String returnUrl = UISvcHelpers.serverCfg().alipayReturnURL();
    String notifyUrl = UISvcHelpers.serverCfg().alipayNotifyURL();
    String pageContent = AlipayHelpers.test1RandTraceNo(
      cfg,
      _orderId, product.getName(), total,
      returnUrl, notifyUrl
    );
    String htmlPage = String.format(htmlTemplate, pageContent);
    wvAlipay.getEngine().loadContent(htmlPage);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    wvAlipay.getEngine().setUserStyleSheetLocation(
      getClass().getResource("/default.css").toExternalForm()
    );
  }
}
