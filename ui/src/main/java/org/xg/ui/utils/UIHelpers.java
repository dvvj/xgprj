package org.xg.ui.utils;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.xg.ui.UiLoginController;
import org.xg.ui.comp.ErrorMessageCtrl;

import java.io.IOException;
import java.net.URL;

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

  public static void setPlaceHolder4EmptyTreeView(JFXTreeTableView table, String resKey) {
    Label lblPlaceHolder = new Label();
    lblPlaceHolder.setText(
      Global.AllRes.getString(resKey)
    );
//    JFXSpinner spinner = new JFXSpinner();
//    spinner.getStyleClass().add("blue-spinner");
//    spinner.setRadius(20.0);
    VBox p = new VBox();
    p.setAlignment(Pos.CENTER);
    p.getChildren().addAll(lblPlaceHolder);
    table.setPlaceholder(p);
  }

  public static Region loadDialog(String fxmlPath) {
    try {
      URL path = UiLoginController.class.getResource(fxmlPath);
      FXMLLoader loader = new FXMLLoader(path, Global.AllRes);
      return loader.load();
    }
    catch (Exception ex) {
      Global.loggingTodo("failed to load FXML from: " + fxmlPath);
      throw new RuntimeException(ex);
    }
  }

  public static void errorDialog(String errMsg, StackPane container) {
    FXMLLoader errMsgLoader = new FXMLLoader(
      UiLoginController.class.getResource("/ui/comp/dialogs/ErrorMessage.fxml"),
      Global.AllRes
    );

    try {
      JFXDialog dialog = new JFXDialog();
      Pane content = errMsgLoader.load();
      ErrorMessageCtrl ctrl = errMsgLoader.getController();
      ctrl.setErrorMsg(errMsg);
      dialog.setContent(content);
      dialog.show(container);
    }
    catch (IOException ex) {
      ex.printStackTrace();
      Global.loggingTodo("error creating error dialog: " + ex.getMessage());
    }

  }
}
