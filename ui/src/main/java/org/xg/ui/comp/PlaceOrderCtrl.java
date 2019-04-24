package org.xg.ui.comp;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.xg.auth.SvcHelpers;
import org.xg.pay.pricePlan.TPricePlan;
import org.xg.svc.UserOrder;
import org.xg.uiModels.CustomerProduct;
import org.xg.uiModels.UIProduct;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.UISvcHelpers;

import java.net.URL;
import java.util.ResourceBundle;

public class PlaceOrderCtrl implements Initializable {

  @FXML
  private JFXTextField txtQty;

  @FXML
  private Text txtInfo;

//  @FXML
//  private Button btnMinus;
//
//  @FXML
//  private Button btnPlus;

  private BooleanProperty invalidNumber = new SimpleBooleanProperty();

  private ObjectProperty<CustomerProduct> selectedProduct = new SimpleObjectProperty<>();

  public void bindSelectedProduct(ObservableValue<CustomerProduct> product) {
    selectedProduct.bind(product);
  }

  private Runnable postPurchase;
  public void setPostPurchase(Runnable postProcess) {
    postPurchase = postProcess;
  }

  @FXML
  private void handlePurchase(ActionEvent e) {
    if (selectedProduct.getValue() != null) {
      Long newOrderId = null;
      Double actualCost = null;
      Double qty = null;
      CustomerProduct prod = selectedProduct.getValue();
      UIProduct product = prod.getProduct();
      try {
        qty = Double.parseDouble(txtQty.getText());
        TPricePlan pricePlan = prod.getPricePlan(); //Global.getPricePlan();
        double unitCost = pricePlan != null ?
          pricePlan.adjust(
            product.getId(),
            product.getPrice0()
          ) : product.getPrice0();
        actualCost = unitCost*qty;
        UserOrder order = new UserOrder(Global.getCurrUid(), product.getId(), qty, actualCost);
        String orderJson = UserOrder.toJson(order);
        String resp = SvcHelpers.reqPut(
          UISvcHelpers.serverCfg().placeOrderURL(),
          Global.getCurrToken(),
          orderJson
        );
        newOrderId = Long.parseLong(resp);
        Global.loggingTodo(resp);
        //Global.updateAllOrders();
      }
      catch (Exception ex) {
        Global.loggingTodo(ex.getMessage());
        throw new RuntimeException("Error placing order!", ex);
      }

      AlipayWindow.launch(newOrderId, actualCost, product.getName(), qty);

      postPurchase.run();
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
        errorInfo("placeOrder.numShouldGE0");
      }
    }
    catch (Exception ex) {
      System.out.println("error:" + value);
      ex.printStackTrace();
      errorInfo("placeOrder.numFormatErr");
      invalidNumber.setValue(true);
    }
  }

  private void updateInfo(String resKey, Color color) {
    updateInfoTxt(Global.AllRes.getString(resKey), color);
  }
  private void updateInfoTxt(String txt, Color color) {
    txtInfo.setText(txt);
    txtInfo.setFill(color);
  }

  private void errorInfo(String resKey) {
    updateInfo(resKey, Color.RED);
  }

  private void costInfo(Double cost) {
    String fmt = Global.AllRes.getString("placeOrder.costInfo");
    String txt = String.format(fmt, cost);
    updateInfoTxt(txt, Color.GREEN);
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    txtInfo.visibleProperty().bind(invalidNumber);
    //handleValue(txtQty.getText(), 0.0);

    txtQty.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        handleValue(newValue, 0.0);
      }
    });
  }
}
