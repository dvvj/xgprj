package org.xg.ui.comp;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.xg.dbModels.MCustomer;
import org.xg.dbModels.MPricePlanMap;
import org.xg.gnl.DataUtils;
import org.xg.svc.AddNewCustomer;
import org.xg.ui.model.MedProfWndHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiModels.PricePlan;
import org.xg.uiModels.PricePlanOption;
import org.xg.user.UserType;

import java.util.ArrayList;

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
  @FXML
  JFXComboBox<PricePlanOption> cmboPricePlanType;

  public void onAdd() {
    try {
      PricePlanOption pricePlanOption = cmboPricePlanType.getSelectionModel().getSelectedItem();
      String uid = UserType.Customer().genUid(tfUid.getText().trim());
      MPricePlanMap ppm = MedProfWndHelper.isValidPlan(pricePlanOption) ?
        MPricePlanMap.createJ(uid, pricePlanOption.getPlan().id(), DataUtils.utcTimeNowStr(), null) : null;
      System.out.println("MPricePlanMap: " + ppm);
      AddNewCustomer newCustomer = new AddNewCustomer(
        new MCustomer(
          uid,
          tfName.getText(),
          tfIdCardNo.getText(),
          tfMobile.getText(),
          tfPostalAddr.getText(),
          tfBDay.getText(),
          Global.getCurrUid()
        ),
        pfNew.getText(),
        ppm
      );

      UISvcHelpers.addNewCustomer(newCustomer);
      newCustomerCallback.run();
    }
    catch (Exception ex) {
      Global.loggingTodo("Error adding customer: " + ex.getMessage());
    }
  }

  private Runnable newCustomerCallback;

  //private ListProperty<PricePlan> pricePlanList;
  private ListProperty<PricePlanOption> pricePlanOptionList;

//  private final static Integer NA = 0;
  public void setup(
    ObservableList<PricePlanOption> pricePlans,
    Runnable callback
    ) {
    System.out.println("# price plans: " + pricePlans.size());
    //pricePlanList = new SimpleListProperty<>(pricePlans);
    pricePlanOptionList = new SimpleListProperty<>(pricePlans);
    cmboPricePlanType.itemsProperty().bind(pricePlanOptionList);
    newCustomerCallback = callback;
//    Helpers.uiTaskJ(
//      () -> NA,
//      na -> {
//        pricePlanList = new SimpleListProperty<>(pricePlans);
//        cmboPricePlanType.itemsProperty().bind(pricePlanList);
//        return null;
//      },
//      10000
//    );
  }
}
