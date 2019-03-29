package org.xg.ui.comp;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.xg.dbModels.MMedProf;
import org.xg.svc.AddNewMedProf;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.UISvcHelpers;

public class AddNewMedProfCtrl {
  @FXML
  JFXTextField tfUid;
  @FXML
  JFXTextField tfName;
  @FXML
  JFXTextField tfIdCardNo;
  @FXML
  JFXTextField tfMobile;
  @FXML
  JFXPasswordField pfNew;
  @FXML
  JFXPasswordField pfNew2;

  public void onAdd() {
    AddNewMedProf mp = new AddNewMedProf(
      new MMedProf(tfUid.getText(), tfName.getText(), tfIdCardNo.getText(), tfMobile.getText(), Global.getCurrUid()),
      pfNew.getText()
    );
    UISvcHelpers.addNewMedProf(mp);
  }
}
