package org.xg.ui.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.xg.uiModels.UIProduct;

public class NewCustomerProfileUiData {
  private final UIProduct prod;

  private BooleanProperty isInExistingProfile = new SimpleBooleanProperty();
  private BooleanProperty isChecked = new SimpleBooleanProperty();

  public NewCustomerProfileUiData(UIProduct prod) {
    this.prod = prod;
  }

  public UIProduct getProd() {
    return prod;
  }

  public boolean isIsInExistingProfile() {
    return isInExistingProfile.get();
  }

  public BooleanProperty isInExistingProfileProperty() {
    return isInExistingProfile;
  }

  public void setIsInExistingProfile(boolean isInExistingProfile) {
    this.isInExistingProfile.set(isInExistingProfile);
  }

  public boolean isChecked() {
    return isChecked.get();
  }

  public BooleanProperty isCheckedProperty() {
    return isChecked;
  }

  public void setIsChecked(boolean isChecked) {
    this.isChecked.set(isChecked);
  }
}
