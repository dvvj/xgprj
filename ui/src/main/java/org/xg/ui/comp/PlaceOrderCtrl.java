package org.xg.ui.comp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import org.xg.ui.UiLoginController;
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

  @FXML
  private void handlePurchase(ActionEvent e) {
    FXMLLoader loader = new FXMLLoader(
      UiLoginController.class.getResource("/ui/AlipayWebview.fxml"),
      Global.AllRes
    );

    try {
      HBox n = loader.load();
      Scene scene = Global.sceneDefStyle(n);
      Stage payStage = new Stage();
      payStage.setScene(scene);
      payStage.show();
    }
    catch (IOException ex) {
      throw new RuntimeException("Error launching main window!", ex);
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
