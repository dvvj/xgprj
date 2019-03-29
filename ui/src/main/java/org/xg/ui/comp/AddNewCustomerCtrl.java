package org.xg.ui.comp;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.xg.dbModels.MCustomer;
import org.xg.svc.AddNewCustomer;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.UISvcHelpers;

public class AddNewCustomerCtrl {
  @FXML
  JFXTextField tfUid;
  @FXML
  JFXTextField tfName;
  @FXML
  JFXTextField tfIdCardNo;
  @FXML
  JFXTextField tfMobile;
  @FXML
  JFXTextField tfPostalAddr;
  @FXML
  JFXTextField tfBDay;
  @FXML
  JFXPasswordField pfNew;
  @FXML
  JFXPasswordField pfNew2;

  public void onAdd() {
    AddNewCustomer newCustomer = new AddNewCustomer(
      new MCustomer(
        tfUid.getText(),
        tfName.getText(),
        tfIdCardNo.getText(),
        tfMobile.getText(),
        tfPostalAddr.getText(),
        tfBDay.getText(),
        Global.getCurrUid()
      ),
      pfNew.getText()
    );

    UISvcHelpers.addNewCustomer(newCustomer);
  }
}
