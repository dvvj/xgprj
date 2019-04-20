package org.xg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.xg.uiModels.UIProduct;

public class CustomerMainController {

  @FXML
  private TableView<UIProduct> _prodTable;

  @FXML
  public void initialize() {

    //resourcesLabel.setText(resources.getBaseBundleName());
  }

}
