package org.xg.ui.utils;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import org.xg.ui.model.Product;

public class UIHelpers {

  public static void setPlaceHolder4TreeView(JFXTreeTableView table, String resKey) {
    Label lblPlaceHolder = new Label();
    lblPlaceHolder.setText(
      Global.AllRes.getString(resKey)
    );
//    JFXSpinner spinner = new JFXSpinner();
//    spinner.getStyleClass().add("blue-spinner");
//    spinner.setRadius(20.0);
    JFXProgressBar jfxBarInf = new JFXProgressBar();
    jfxBarInf.setPrefWidth(100);
    jfxBarInf.setProgress(-1.0f);
    VBox p = new VBox();
    p.setAlignment(Pos.CENTER);
    p.getChildren().addAll(lblPlaceHolder, jfxBarInf);
    table.setPlaceholder(p);
  }

  public static <T extends RecursiveTreeObject<T>> void setRoot4TreeView(JFXTreeTableView<T> table, ObservableList<T> v) {
    TreeItem<T> items = new RecursiveTreeItem<T>(v, RecursiveTreeObject::getChildren);
    table.setRoot(items);
    table.setShowRoot(false);
  }
}
