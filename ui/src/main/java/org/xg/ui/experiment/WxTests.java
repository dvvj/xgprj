package org.xg.ui.experiment;

import com.github.binarywang.wxpay.service.WxPayService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.xg.weixin.WxUtils;

import java.io.ByteArrayInputStream;

public class WxTests extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  private static WxPayService wxPayService = WxUtils.createWxSvc(
    "wx6f58f5f5ff06f57f",
    "1409382102",
    "/home/devvj/.weixin/apikey.txt",
    "/home/devvj/.weixin/apiclient_cert.p12",
    false
  );

  @Override
  public void start(Stage stage) throws Exception {

    int qrImgSize = 400;
    byte[] qrCode = WxUtils.createOrder(
      wxPayService,
      1,
      "商品 info",
      "prodId001",
      "https://todo/notfiy",
      qrImgSize
    );
    ByteArrayInputStream in = new ByteArrayInputStream(qrCode);

    BorderPane root = new BorderPane();
    Image image = new Image(in);
    ImageView view = new ImageView(image);
    view.setStyle("-fx-stroke-width: 2; -fx-stroke: blue");
    root.setCenter(view);
    Scene scene = new Scene(root, qrImgSize, qrImgSize);
    stage.setScene(scene);
    stage.show();
  }
}
