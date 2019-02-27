package org.xg.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.xg.pay.AlipayCfg;
import org.xg.pay.AlipayHelpers;
import org.xg.ui.utils.Global;

public class WebviewTest extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    Pane root = new HBox();
    WebView wv = new WebView();
    wv.getEngine().setUserStyleSheetLocation(
      getClass().getResource("/default.css").toExternalForm()
    );
    root.getChildren().add(wv);

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
