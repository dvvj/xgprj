package org.xg.ui.comp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.utils.JFXHighlighter;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;

import java.util.Set;
import java.util.function.*;

public class TreeTableViewWithFilterCtrl<T extends RecursiveTreeObject<T>> {

  private JFXHighlighter highlighter = new JFXHighlighter();

  private String emptyTableRes;

  public void setup(
//    String headingRes,
    String refreshRes,
    String searchRes,
    String filterRes,
    String emptyTableRes,
    Function<T, Set<String>> searchStringCollector
  ) {
    this.emptyTableRes = emptyTableRes;
//    Global.setResText(txtHeading, headingRes);
    txtSearch.setPromptText(Global.AllRes.getString(searchRes));
    btnRefresh.setText(Global.AllRes.getString(refreshRes));
//    btnFilter.setText(Global.AllRes.getString(filterRes));

    txtSearch.setOnKeyReleased(e -> {
      filterAndUpdateTable2(
        t -> {
          Set<String> strs = searchStringCollector.apply((T)t);
          return strs.stream().anyMatch(s -> s.toLowerCase().contains(txtSearch.getText()));
        }
      );
    });
//    btnFilter.setOnAction(e -> filterAndUpdateTable2(
//      t -> {
//        Set<String> strs = searchStringCollector.apply((T)t);
//        return strs.stream().anyMatch(s -> s.toLowerCase().contains(txtSearch.getText()));
//      }
//    ));
  }

  private void filterAndUpdateTable2(Predicate<T> filter) {
    Task<Integer> task = Helpers.uiTaskJ(
      () -> {
        return 0;
      },
      resp -> {
        filterAndUpdateTable(filter);
        //highlighter.highlight(theTable, txtSearch.getText());
        return null;
      },
      1000
    );
    new Thread(task).start();
//    if (productsCache != null && productsCache.size() > 0) {
//      activeProducts = productsCache.filtered(filter);
//
//      UIHelpers.setRoot4TreeView(tblProducts, activeProducts);
//      Global.loggingTodo(String.format("found %d products", activeProducts.size()));
//      //tblProducts.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener(productsCache));
//      if (activeProducts.size() > 0) {
//        TreeItem<Product> first =  tblProducts.getTreeItem(0);
//        tblProducts.getSelectionModel().select(first);
//        updateSelection(first.getValue());
//      }
//    }
  }

  private ObservableList<T> dataCache;
  private ObservableList<T> activeDataCache;

  public void setupColumsAndLoadData(
    Consumer<JFXTreeTableView<T>> columnBuilder,
    Supplier<ObservableList<T>> dataRetriever
  ) {
    //UIHelpers.setPlaceHolder4TreeView(theTable, "customerTable.placeHolder");
    columnBuilder.accept(theTable);

    Task<ObservableList<T>> fetchCustomersTask = Helpers.uiTaskJ(
      () -> {
        try {
          //Thread.sleep(1000);
          return dataRetriever.get();
        }
        catch (Exception ex) {
          Global.loggingTodo(
            String.format(
              "Error fetching table for [%s]: %s", Global.getCurrUid(), ex.getMessage()
            )
          );
          return null;
        }
      },
      resp -> {
        if (resp != null) {
          dataCache = resp;
//          UIHelpers.setRoot4TreeView(theTable, dataCache);
//
//          if (dataCache.size() > 0) {
//            theTable.getSelectionModel().select(0);
//          }
          filterAndUpdateTable(t -> true);
        }
        else {
          // todo: show error
        }
        return null;
      },
      30000
    );

    new Thread(fetchCustomersTask).start();
  }

  private void filterAndUpdateTable(Predicate<T> filter) {
    if (dataCache != null && dataCache.size() > 0) {
      highlighter.clear();
      activeDataCache = dataCache.filtered(filter);

      UIHelpers.setRoot4TreeView(theTable, activeDataCache);
      Global.loggingTodo(String.format("found %d entries", activeDataCache.size()));
      //tblProducts.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener(productsCache));
      if (activeDataCache.size() > 0) {
        TreeItem<T> first =  theTable.getTreeItem(0);
        theTable.getSelectionModel().select(first);
        //updateSelection(first.getValue());
      }
      else {
        UIHelpers.setPlaceHolder4EmptyTreeView(theTable, emptyTableRes);
      }

      if (!txtSearch.getText().isEmpty()) {
        System.out.println("highlighting..." + txtSearch.getText());
        highlighter.highlight(theTable, txtSearch.getText());
      }

      //theTable.group(theTable.getColumns().get(0));
    }
    else {
      UIHelpers.setPlaceHolder4EmptyTreeView(theTable, emptyTableRes);
    }
  }

  @FXML
  private Text txtHeading;
  @FXML
  private JFXButton btnRefresh;
  @FXML
  private JFXTextField txtSearch;
//  @FXML
//  private JFXButton btnFilter;
  @FXML
  private JFXTreeTableView<T> theTable;

}
