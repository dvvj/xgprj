package org.xg.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.xg.pay.AlipayCfg;
import org.xg.pay.AlipayHelpers;
import org.xg.ui.utils.Global;

public class WebviewTest extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    HBox root = new HBox();
    root.setSpacing(20);
    WebView wv = new WebView();
    wv.getEngine().setUserStyleSheetLocation(
      getClass().getResource("/default.css").toExternalForm()
    );

    Button btn = new Button();
    btn.setText("inspect");
    btn.setOnAction(e -> {
      //Document doc = wv.getEngine().getDocument();
      String html = (String) wv.getEngine().executeScript("document.documentElement.outerHTML");
      System.out.println(html);
    });

    root.getChildren().addAll(btn, wv);

    AlipayCfg cfg = AlipayHelpers.testLocalCfg();
    String pageContent = AlipayHelpers.test1(cfg);
    wv.getEngine().loadContent(pageContent);

    stage.setScene(
      //new Scene(root, 400, 300)
      Global.sceneDefStyle(root)
    );

    stage.show();
  }
}
