package org.xg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;
import org.xg.ui.model.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerMainController {

  @FXML
  private TableView<Product> _prodTable;

  @FXML
  public void initialize() {

    //resourcesLabel.setText(resources.getBaseBundleName());
  }

}
