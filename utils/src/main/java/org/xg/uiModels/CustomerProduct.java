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


  public Double getActualPrice() {
    return pricePlan != null ? pricePlan.adjust(
      getProduct().getId(), getProduct().getPrice0()
    ) : getProduct().getPrice0();
  }

  public String getPriceDetail() {
    if (pricePlan != null) {
      Double actualPrice = getActualPrice();
      Long discount = Math.round((1 - actualPrice / getProduct().getPrice0())*100);
      return String.format(
        "%.2f (省%d%%)", actualPrice, discount
      );
    }
    else {
      return getProduct().getPrice0().toString();
    }
  }
}
