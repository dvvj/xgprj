package org.xg.ui.comp;

import com.jfoenix.controls.*;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.xg.dbModels.*;
import org.xg.gnl.DataUtils;
import org.xg.svc.AddNewCustomer;
import org.xg.ui.model.MedProfWndHelper;
import org.xg.ui.model.NewCustomerProfileUiData;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiDataModels.DMRepo.DMFindCustomer;
import org.xg.uiDataModels.DataLoaders;
import org.xg.uiModels.PricePlan;
import org.xg.uiModels.PricePlanOption;
import org.xg.uiModels.UIProduct;
import org.xg.user.UserType;

import javax.xml.crypto.Data;
import java.util.*;

import static org.xg.gnl.DataUtils.*;

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

  @FXML
  JFXCheckBox cbIsNewCustomer;

  @FXML
  JFXListView<JFXCheckBox> lstProducts;

  private Map<Integer, NewCustomerProfileUiData> productIdxMap = new HashMap<>();

  private static String productText(UIProduct product) {
    String srcCountry = Helpers.srcCountryResKey(product.getDetail().getSrcCountry());
    return String.format(
      "[%s] %s", Global.AllRes.getString(srcCountry), product.getName()
    );
  }

  private void loadProductList(ObservableList<UIProduct> products) {
    productIdxMap.clear();

    for (Integer idx = 0; idx < products.size(); idx ++) {
      NewCustomerProfileUiData prodData = new NewCustomerProfileUiData(products.get(idx));

      productIdxMap.put(idx, prodData);
      JFXCheckBox cb = new JFXCheckBox(productText(prodData.getProd()));
      cb.selectedProperty().bindBidirectional(prodData.isCheckedProperty());
      cb.disableProperty().bindBidirectional(prodData.isInExistingProfileProperty());
      lstProducts.getItems().add(cb);
    }
  }

  private int[] getSelectedProducts() {
    List<Integer> selectedProdIds = new ArrayList<>();
//    for (Integer idx = 0; idx < lstProducts.getItems().size(); idx++) {
//      if (lstProducts.getItems().get(idx).isSelected())
//        selectedProdIds.add(idx);
//    }
//    int[] res = new int[selectedProdIds.size()];
//    for (int idx = 0; idx < selectedProdIds.size(); idx ++) {
//      res[idx] = productIdxMap.get(selectedProdIds.get(idx)).getId();
//    }
    for (NewCustomerProfileUiData d : productIdxMap.values()) {
      if (!d.isIsInExistingProfile() && d.isChecked())
        selectedProdIds.add(d.getProd().getId());
    }
    int[] res = new int[selectedProdIds.size()];
    for (int idx = 0; idx < selectedProdIds.size(); idx ++) {
      res[idx] = selectedProdIds.get(idx);
    }
    return res;
  }

  private void updateExistingCustomerInfo(DMFindCustomer fc) {
    MCustomer customer = fc.customer();
    tfName.setText(maskStrStart(customer.name(), 1));
    tfMobile.setText(maskStr(customer.mobile(), 3, 2));
    tfIdCardNo.setText(maskStrAll(customer.idCardNo()));
    tfBDay.setText(maskStrAll(customer.bday()));
    tfPostalAddr.setText(maskStrAll(customer.postalAddr()));
    pfNew.setText("***");
    pfNew2.setText("***");

    MCustomerProfile[] profiles = fc.profiles();
    Set<Integer> existingProdIds = new HashSet<>();
    Arrays.stream(profiles).forEach( p -> {
        for (int id: p.getDetail().productIds()) {
          existingProdIds.add(id);
        }
      });
    for (NewCustomerProfileUiData d : productIdxMap.values()) {
      UIProduct prod = d.getProd();
      boolean isInExisting = existingProdIds.contains(prod.getId());
      setStatus(d, isInExisting);
    }
  }

  private static void setStatus(NewCustomerProfileUiData d, boolean isInExistingProfile) {
    d.setIsInExistingProfile(isInExistingProfile);
    d.setIsChecked(isInExistingProfile);
  }

  public void onCheckExisting() {
    //System.out.println("todo");
    String uid = tfUid.getText().trim();
    DMFindCustomer fc =
      DataLoaders.findCustomerDataLoader(
        UISvcHelpers.serverCfg(), Global.getCurrToken(), uid
      ).loadAndConstruct(20000);
    //MCustomer c = fc.customer();
    //MCustomer c = UISvcHelpers.findCustomerById(uid);
    if (fc.customer() != null) {
      updateExistingCustomerInfo(fc);
    }
  }

  @FXML
  StackPane rootStackPane;

  public void onAdd() {
    try {
      String uid = UserType.Customer().genUid(tfUid.getText().trim());
      PricePlanOption pricePlanOption = cmboPricePlanType.getSelectionModel().getSelectedItem();

      MPricePlanMap ppm = MedProfWndHelper.isValidPlan(pricePlanOption) ?
        MPricePlanMap.createJ(uid, pricePlanOption.getPlan().id(), DataUtils.utcTimeNowStr(), null) : null;
      System.out.println("MPricePlanMap: " + ppm);
      if (cbIsNewCustomer.isSelected()) {
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

        OpResp resp = UISvcHelpers.addNewCustomer(newCustomer);
        if (resp.success()) {
          newCustomerCallback.run();
          Global.loggingTodo("New customer added: " + uid);
        }
        else {
          Global.loggingTodo("Failed to add new customer: " + resp.errMsgJ());
        }
      }

      if (ppm != null && ppm.getPlanIds().length > 1)
        throw new UnsupportedOperationException("Not supported: multiple plan ids");
      String planId = ppm != null ? ppm.getPlanIds()[0] : null;
      long newProfileId = UISvcHelpers.createProfileV1_00(
        Global.getCurrUid(), uid,
        getSelectedProducts(), //new int[] { 1, 3 }, // todo
        planId
      );
//      Global.loggingTodo(
//        String.format("New profile created for customer [%s]: %d", uid, newProfileId)
//      );

      String msg = String.format("New profile created for customer [%s]: %d", uid, newProfileId);

      JFXDialog dialog = new JFXDialog();
      dialog.setContent(
        UIHelpers.loadDialog("/ui/comp/dialogs/AddNewCustomerProfile_Success.fxml")
      );
      dialog.show(rootStackPane);
    }
    catch (Exception ex) {
      Global.loggingTodo("Error adding customer: " + ex.getMessage());
    }
  }

  private Runnable newCustomerCallback;

  //private ListProperty<PricePlan> pricePlanList;
  private ListProperty<PricePlanOption> pricePlanOptionList;

  @FXML
  JFXButton btnCheckExisting;
  private void bindControls() {
    btnCheckExisting.disableProperty().bind(cbIsNewCustomer.selectedProperty());
    BooleanBinding notNewCustomer = cbIsNewCustomer.selectedProperty().not();
    tfName.disableProperty().bind(notNewCustomer);
    tfIdCardNo.disableProperty().bind(notNewCustomer);
    tfMobile.disableProperty().bind(notNewCustomer);
    tfPostalAddr.disableProperty().bind(notNewCustomer);
    tfBDay.disableProperty().bind(notNewCustomer);
    pfNew.disableProperty().bind(notNewCustomer);
    pfNew2.disableProperty().bind(notNewCustomer);
  }

//  private final static Integer NA = 0;
  public void setup(
    ObservableList<PricePlanOption> pricePlans,
    ObservableList<UIProduct> products,
    Runnable callback
    ) {
    System.out.println("# price plans: " + pricePlans.size());
    //pricePlanList = new SimpleListProperty<>(pricePlans);
    pricePlanOptionList = new SimpleListProperty<>(pricePlans);
    cmboPricePlanType.itemsProperty().bind(pricePlanOptionList);
    newCustomerCallback = callback;
    loadProductList(products);
    bindControls();
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
