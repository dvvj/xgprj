package org.xg.ui.comp;

import com.github.binarywang.wxpay.service.WxPayService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.xg.ui.UiLoginController;
import org.xg.ui.utils.Global;
import org.xg.weixin.WxUtils;

import java.io.ByteArrayInputStream;

public class WeixinPayWindow {
  private static final int QRImgSize = 400;

  public static void launch(
    WxPayService wxPayService,
    String orderBody,
    String prodId,
    double actualCost,
    String notifyUrl
  ) {

    FXMLLoader loader = new FXMLLoader(
      UiLoginController.class.getResource("/ui/comp/WeixinPayWnd.fxml"),
      Global.AllRes
    );

    try {
      StackPane n = loader.load();
      WeixinPayCtrl ctrl = loader.getController();
      byte[] qrCode = WxUtils.createOrder(
        wxPayService,
        1, // todo: actualCost*100  yuan -> fen
        orderBody,
        prodId,
        notifyUrl,
        QRImgSize
      );

      ctrl.setQrCode(qrCode);
      Scene scene = Global.sceneDefStyle(n);
      Stage payStage = new Stage();
      payStage.setScene(scene);
      payStage.show();

    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    //view.setStyle("-fx-stroke-width: 2; -fx-stroke: blue");
  }
}
