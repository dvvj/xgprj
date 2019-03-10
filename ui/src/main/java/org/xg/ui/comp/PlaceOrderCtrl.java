package org.xg.ui.comp;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.xg.auth.SvcHelpers;
import org.xg.gnl.GlobalCfg;
import org.xg.svc.UserOrder;
import org.xg.ui.UiLoginController;
import org.xg.ui.model.Product;
import org.xg.ui.utils.Global;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlaceOrderCtrl implements Initializable {

  @FXML
  private TextField txtQty;

  @FXML
  private Text txtError;

//  @FXML
//  private Button btnMinus;
//
//  @FXML
//  private Button btnPlus;

  private BooleanProperty invalidNumber = new SimpleBooleanProperty();

  private ObjectProperty<Product> selectedProduct = new SimpleObjectProperty<>();

  public void bindSelectedProduct(ObservableValue<Product> product) {
    selectedProduct.bind(product);
  }

  @FXML
  private void handlePurchase(ActionEvent e) {
    if (selectedProduct.getValue() != null) {
      FXMLLoader loader = new FXMLLoader(
        UiLoginController.class.getResource("/ui/AlipayWebview.fxml"),
        Global.AllRes
      );

      try {
        Double qty = Double.parseDouble(txtQty.getText());
        UserOrder order = new UserOrder(Global.getCurrUid(), selectedProduct.getValue().getId(), qty);
        String orderJson = UserOrder.toJson(order);
        String resp = SvcHelpers.reqPut(
          Global.getServerCfg().placeOrderURL(),
          Global.getCurrToken(),
          orderJson
        );
        Global.loggingTodo(resp);
        Global.updateAllOrders();
      }
      catch (Exception ex) {
        Global.loggingTodo(ex.getMessage());
        throw new RuntimeException("Error placing order!", ex);
      }

      try {
        HBox n = loader.load();
        AlipayWebviewCtrl ctrl = loader.getController();
        ctrl.setAmountAndSendReq(selectedProduct.getValue(), txtQty.textProperty());
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
    else {
      System.out.println("Selected product is null");
    }

  }

  @FXML
  private void handleMinus1(ActionEvent e) {
    handleValue(txtQty.getText(), -1.0);
  }

  @FXML
  private void handlePlus1(ActionEvent e) {
    handleValue(txtQty.getText(), 1.0);
  }

  private boolean hasDelta(Double delta) {
    return delta != null && Math.abs(delta) > 1e-8;
  }

  private void handleValue(String value, Double delta) {
    try {
      Double v = Double.parseDouble(value) + delta;
      if (v > 0) {
        String txtVal = String.format("%.2f", v);
        if (hasDelta(delta))
          txtQty.setText(txtVal);
        invalidNumber.setValue(false);
      }
      else {
        invalidNumber.setValue(true);
        txtError.setText(Global.AllRes.getString("placeOrder.numShouldGE0"));
      }
    }
    catch (Exception ex) {
      txtError.setText(Global.AllRes.getString("placeOrder.numFormatErr"));
      invalidNumber.setValue(true);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    txtError.visibleProperty().bind(invalidNumber);

    txtQty.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        handleValue(newValue, 0.0);
      }
    });
  }
}
