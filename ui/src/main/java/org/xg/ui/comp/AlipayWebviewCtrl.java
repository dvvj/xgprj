package org.xg.ui.comp;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xg.alipay.NotifyUtils;
import org.xg.pay.AlipayCfg;
import org.xg.pay.AlipayHelpers;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UISvcHelpers;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
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

  private double amount;
  //private double unitPrice;
  private Long _orderId;

  public void setOrderInfoAndSendReq(Long orderId, double actualCost, String productName, Double qty) {
    amount = qty;
    _orderId = orderId;
    //this.unitPrice = product.getActualPrice();

    //String privateKeyPath = "/home/devvj/alipay-keys/rsa_private_key.raw";
    AlipayCfg cfg = AlipayHelpers.testLocalCfg();
    //Double total = unitPrice * amount; //Double.parseDouble(amount.getValue());
    //String returnUrl = "http://wonder4.life/webapi/payment/alipayReturn";
    String returnUrl = UISvcHelpers.serverCfg().alipayReturnURL();
    String notifyUrl = UISvcHelpers.serverCfg().alipayNotifyURL();
    //String notifyUrl = "http://wonder4.life/webapi/payment/alipayNotify";
    Global.loggingTodo(
      String.format("return url: %s\nnotify url: %s", returnUrl, notifyUrl)
    );
    String pageContent = AlipayHelpers.test1RandTraceNo(
      cfg,
      _orderId, productName, actualCost,
      returnUrl, notifyUrl
    );
    String htmlPage = String.format(htmlTemplate, pageContent);
    wvAlipay.getEngine().loadContent(htmlPage);

    wvAlipay.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == Worker.State.SUCCEEDED) {
        System.out.println("finished loading===================");
        //System.out.println(wvAlipay.getEngine().getDocument());
        Document doc = wvAlipay.getEngine().getDocument();
        if (doc != null) {
          Element retPageDiv = doc.getElementById(NotifyUtils.returnPageId());
          transformDoc(doc);
          if (retPageDiv != null) {
            System.out.println("found return page!");
          }
        }
      }
    });

//    Helpers.monitorAlipayReturn(
//      () -> {
//        wvAlipay.getEngine().getDocument().getdo
//      },
//      600
//    );
  }

  private static void transformDoc(Document doc) {
    try {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      StringWriter stringWriter = new StringWriter();
      try {
        transformer.transform(new DOMSource(doc),
          new StreamResult(stringWriter));
      } catch (TransformerException ex) {
        System.out.println("error: " + ex.getMessage());
      }
      String xml1 = stringWriter.getBuffer().toString();
      System.out.println("res xml: ");
      System.out.println(xml1);


    }catch (TransformerConfigurationException ex) {
      System.out.println("error: " + ex.getMessage());
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    wvAlipay.getEngine().setUserStyleSheetLocation(
      getClass().getResource("/default.css").toExternalForm()
    );
  }
}
