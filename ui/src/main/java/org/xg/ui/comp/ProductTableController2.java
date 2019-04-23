package org.xg.ui.comp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.xg.svc.ImageInfo;
import org.xg.ui.utils.UISvcHelpers;
import org.xg.uiModels.UIProduct;

public class ProductTableController2 {
  private StringProperty selectedProductDetail = new SimpleStringProperty();
  public StringProperty getSelectedProductDetail() {
    return selectedProductDetail;
  }

  private StringProperty selectedProductImageUrl = new SimpleStringProperty();
  public StringProperty getSelectedProductImageUrl() {
    return selectedProductImageUrl;
  }

  private ObjectProperty<UIProduct> selectedProduct = new SimpleObjectProperty<>();
  public ObservableValue<UIProduct> getSelectedProduct() {
    return selectedProduct;
  }

  private final TreeTableViewWithFilterCtrl<UIProduct> treeTableCtrl;

  public ProductTableController2(TreeTableViewWithFilterCtrl<UIProduct> treeTableCtrl) {
    this.treeTableCtrl = treeTableCtrl;
    selectedProduct.addListener(new ItemSelectChangeListener<>());
    selectedProduct.bind(this.treeTableCtrl.getSelected());
    if (this.treeTableCtrl.getSelected().getValue() != null)
      updateSelection(this.treeTableCtrl.getSelected().getValue());
  }

  private class ItemSelectChangeListener<T extends UIProduct> implements ChangeListener<T> {
    //    private final ObservableList<UIProduct> products;
    ItemSelectChangeListener() { }
    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {

      boolean hasUpdate = newValue != null && oldValue != null && !oldValue.getId().equals(newValue.getId());

      if (hasUpdate) {
        UIProduct prod = newValue;

        updateSelection(prod);
      }

    }
  }

  private void updateSelection(UIProduct prod) {
    selectedProductDetail.setValue(prod.getDetail().getDesc());
    System.out.println("new selection: " + prod.getName());
    System.out.println("\timage: " + prod.getAssets().get(0).url());
    ImageInfo imgInfo = new ImageInfo(prod.getId(), prod.getAssets().get(0).url());
    String url = imgInfo.getUrl(UISvcHelpers.serverCfg());
    //selectedProduct.setValue(prod);
    selectedProductImageUrl.setValue(url);
  }
}
