package org.xg.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.xg.auth.SvcHelpers;
import org.xg.ui.comp.PlaceOrderCtrl;
import org.xg.uiModels.CustomerProduct;
import org.xg.uiModels.UIProduct;
import org.xg.ui.utils.Global;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerMainRCtrl implements Initializable {
  @FXML
  VBox vboxMainR;

  @FXML
  private ImageView img;

  @FXML
  private TextArea txtDetails;

  @FXML
  private Label lblUrl;
  //private StringProperty imgUrlProp = new SimpleStringProperty();

//  private HBox hbPlaceOrder;
  private PlaceOrderCtrl poCtrl;

  //private Runnable postPlaceOrder;

  public void setBinding(
    ObservableValue<String> details,
    ObservableValue<String> imageUrl,
    ObservableValue<CustomerProduct> selectedProduct,
    Runnable postProcess
  ) {
    txtDetails.textProperty().bind(details);
    lblUrl.textProperty().bind(imageUrl);
    poCtrl.bindSelectedProduct(selectedProduct);
    //postPlaceOrder = postProcess;
    poCtrl.setPostPurchase(postProcess);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/ui/PlaceOrder.fxml"),
      Global.AllRes
    );

    try {
      VBox vbox = loader.load();
      poCtrl = loader.getController();
      vboxMainR.getChildren().add(vbox);
    }
    catch (IOException ex) {
      throw new RuntimeException("Error loading ProductPlaceOrderTableCell", ex);
    }


    lblUrl.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        //System.out.println("New value: " + newValue);
        if (newValue != null && !newValue.equals(oldValue)) {
          System.out.println("updating image");
//          Image image = new Image(lblUrl.getText(), 200, 400, true, true);
//          Image image = new Image()
          byte[] bytes = SvcHelpers.get4Bin(lblUrl.getText(), "");
          img.setImage(new Image(new ByteArrayInputStream(bytes), 300, 300, true, true));

        }
      }
    });


//    img.setImage(
////      new Image("file:///media/sf_vmshare/assets/4.png", 200, 400, true, true)
//      new Image("https://localhost:443/webapi/asset/img?prodId=4&imageName=4.png", 200, 400, true, true)
//    );
  }
}
