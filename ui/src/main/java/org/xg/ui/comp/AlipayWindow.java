package org.xg.ui.comp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.xg.ui.UiLoginController;
import org.xg.ui.utils.Global;
import org.xg.uiModels.Product;

import java.io.IOException;

public class AlipayWindow {

  public static void launch(
    Long orderId, double actualCost, String productName, Double qty
  ) {
    FXMLLoader loader = new FXMLLoader(
      UiLoginController.class.getResource("/ui/AlipayWebview.fxml"),
      Global.AllRes
    );

    try {
      HBox n = loader.load();
      AlipayWebviewCtrl ctrl = loader.getController();
      ctrl.setOrderInfoAndSendReq(orderId, actualCost, productName, qty);
      Scene scene = Global.sceneDefStyle(n);
      Stage payStage = new Stage();
      payStage.setScene(scene);
      payStage.show();
    }

    catch (IOException ex) {
      Global.loggingTodo(ex.getMessage());
      throw new RuntimeException("Error launching main window!", ex);
    }
  }
}
