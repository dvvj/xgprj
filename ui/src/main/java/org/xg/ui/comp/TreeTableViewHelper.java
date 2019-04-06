package org.xg.ui.comp;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.xg.ui.UiLoginController;
import org.xg.ui.model.TableViewHelper;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.UIHelpers;
import org.xg.uiModels.CustomerOrder;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TreeTableViewHelper {
  private final static String ResRefresh = ".toolbar.refresh";
  private final static String ResSearchPrompt = ".toolbar.searchPrompt";
  private final static String ResFilter = ".toolbar.filter";
  private final static String ResEmptyPlaceHolder = ".emptyPlaceHolder";
  public static <T extends RecursiveTreeObject<T>> TreeTableViewWithFilterCtrl<T> loadTableToTab(
    Pane tab,
    Function<T, Set<String>> getSearchableStrs,
    Supplier<ObservableList<T>> dataRetriever,
    Runnable uiUpdater,
    String resPfx,
    String resEmptyTablePlaceHold,
    Consumer<T> selectedListener,
    List<JFXTreeTableColumn<T, ?>> columns
  ) throws Exception {
    URL path = UiLoginController.class.getResource("/ui/comp/TreeTableViewWithFilter.fxml");
    FXMLLoader tableLoader = new FXMLLoader(path, Global.AllRes);
    VBox table;

    table = tableLoader.load();
    TreeTableViewWithFilterCtrl<T> tblCtrl = tableLoader.getController();
    tblCtrl.setup(
      resPfx + ResRefresh,
      resPfx + ResSearchPrompt,
      resPfx + ResFilter,
      resPfx + ResEmptyPlaceHolder,
      getSearchableStrs
    );
    tblCtrl.getSelected().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        selectedListener.accept(newValue);
      }
    });

    tblCtrl.setupColumsAndLoadData(
      theTable -> {
        theTable.getColumns().addAll(columns);
        UIHelpers.setPlaceHolder4TreeView(theTable, resEmptyTablePlaceHold);
      },
      dataRetriever,
      uiUpdater
    );

    tab.getChildren().addAll(table);

    return tblCtrl;
  }

  public static <T extends RecursiveTreeObject<T>> TreeTableViewWithFilterCtrl<T> loadTableToTab(
    Pane tab,
    Function<T, Set<String>> getSearchableStrs,
    Supplier<ObservableList<T>> dataRetriever,
    String resPfx,
    String resEmptyTablePlaceHold,
    Consumer<T> selectedListener,
    List<JFXTreeTableColumn<T, ?>> columns
  ) throws Exception {
    return loadTableToTab(tab, getSearchableStrs, dataRetriever,
      () -> {},
      resPfx, resEmptyTablePlaceHold, selectedListener, columns
    );
  }
}