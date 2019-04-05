package org.xg.ui.comp;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.xg.dbModels.MMedProf;
import org.xg.svc.AddNewMedProf;
import org.xg.ui.model.PricePlanOption;
import org.xg.ui.model.RewardPlanOption;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.user.UserType;

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
  @FXML
  JFXComboBox cmboRewardPlanType;

  public void onAdd() {
    String uid = UserType.MedProf().genUid(tfUid.getText().trim());
    AddNewMedProf mp = new AddNewMedProf(
      new MMedProf(uid, tfName.getText(), tfIdCardNo.getText(), tfMobile.getText(), Global.getCurrUid()),
      pfNew.getText()
    );
    UISvcHelpers.addNewMedProf(mp);
  }

  private Runnable newMedProfCallback;
  private ListProperty<RewardPlanOption> pricePlanOptionList;
  public void setup(
    ObservableList<RewardPlanOption> rewardPlans,
    Runnable callback
  ) {
    System.out.println("# price plans: " + rewardPlans.size());
    //pricePlanList = new SimpleListProperty<>(pricePlans);
    pricePlanOptionList = new SimpleListProperty<>(rewardPlans);
    cmboRewardPlanType.itemsProperty().bind(pricePlanOptionList);
    newMedProfCallback = callback;

  }


}
