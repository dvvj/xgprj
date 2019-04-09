package org.xg.ui.comp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.xg.ui.UiLoginController;
import org.xg.ui.utils.Global;

import javax.annotation.PostConstruct;
import java.net.URL;

public class UpdatePasswordCtrl {
  @FXML
  JFXButton btnUpdatePass;

  public static void load2Tab(Pane parent) throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/UpdatePassword.fxml");
    FXMLLoader updatePassLoader = new FXMLLoader(path, Global.AllRes);
    VBox updatePass = updatePassLoader.load();

    parent.getChildren().addAll(updatePass);
  }

  @FXML
  JFXPasswordField pfOld;
  @FXML
  JFXPasswordField pfNew1;
  @FXML
  JFXPasswordField pfNew2;

  @PostConstruct
  public void initialize() {
    pfNew1.textProperty().addListener((observable, oldValue, newValue) -> {
      checkNewPasswordMatch();
    });
    pfNew2.textProperty().addListener((observable, oldValue, newValue) -> {
      checkNewPasswordMatch();
    });

    checkNewPasswordMatch();

    //newpassMismatch.setValue(false);
    btnUpdatePass.disableProperty().bind(newpassMismatch);
    btnUpdatePass.textProperty().bind(btnTextProp);
  }

  public void onUpdatePass(ActionEvent e) {
    Global.loggingTodo("updading pass");
  }

  private BooleanProperty newpassMismatch = new SimpleBooleanProperty();
  private StringProperty btnTextProp = new SimpleStringProperty();

  private void checkNewPasswordMatch() {
    String newpass1 = pfNew1.getText();
    String newpass2 = pfNew2.getText();
    boolean newPassEmpty = newpass1 == null || newpass1.isEmpty();
    boolean passOk = (!newPassEmpty && newpass1.equals(newpass2));

    btnTextProp.setValue(
      Global.AllRes.getString(
        newPassEmpty ? "updatePassword.newPasswordCannotBeNull" :
          passOk ? "updatePassword.doUpdate" : "updatePassword.newPasswordMismatch"
      )
    );

    newpassMismatch.setValue(!passOk);
  }

}
