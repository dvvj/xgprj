package org.xg.ui.comp;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MRewardPlan;
import org.xg.dbModels.MRewardPlanMap;
import org.xg.dbModels.OpResp;
import org.xg.gnl.DataUtils;
import org.xg.svc.AddNewMedProf;
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
  JFXComboBox<RewardPlanOption> cmboRewardPlanType;

  @FXML
  StackPane containerAddNewMedProf;

  public void onAdd() {
    RewardPlanOption rewardPlanOption = cmboRewardPlanType.getSelectionModel().getSelectedItem();
    String uid = UserType.MedProf().genUid(tfUid.getText().trim());
    MRewardPlanMap rpm = RewardPlanOption.isValidPlan(rewardPlanOption) ?
      MRewardPlanMap.createJ(uid, rewardPlanOption.getPlan().id(), DataUtils.utcTimeNowStr(), null) : null;
    System.out.println("MRewardPlanMap: " + rpm);

    AddNewMedProf mp = new AddNewMedProf(
      new MMedProf(uid, tfName.getText(), tfIdCardNo.getText(), tfMobile.getText(), Global.getCurrUid()),
      pfNew.getText(),
      rpm
    );
    UISvcHelpers.addNewMedProf(mp, containerAddNewMedProf);
  }

  private Runnable newMedProfCallback;
  private ListProperty<RewardPlanOption> rewardPlanOptionList;
  public void setup(
    ObservableList<RewardPlanOption> rewardPlans,
    Runnable callback
  ) {
    System.out.println("# price plans: " + rewardPlans.size());
    //pricePlanList = new SimpleListProperty<>(pricePlans);
    rewardPlanOptionList = new SimpleListProperty<>(rewardPlans);
    cmboRewardPlanType.itemsProperty().bind(rewardPlanOptionList);
    newMedProfCallback = callback;

  }


}
