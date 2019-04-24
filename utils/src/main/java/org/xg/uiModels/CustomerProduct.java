package org.xg.uiModels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.pay.pricePlan.TPricePlan;

public class CustomerProduct extends RecursiveTreeObject<CustomerProduct> {
  private UIProduct product;

  private TPricePlan pricePlan;

  public CustomerProduct() { }

  public CustomerProduct(
    UIProduct product,
    TPricePlan pricePlan
  ) {
    this.product = product;
    this.pricePlan = pricePlan;
  }

  public UIProduct getProduct() {
    return product;
  }

  public void setProduct(UIProduct product) {
    this.product = product;
  }

  public TPricePlan getPricePlan() {
    return pricePlan;
  }

  public void setPricePlan(TPricePlan pricePlan) {
    this.pricePlan = pricePlan;
  }
}
