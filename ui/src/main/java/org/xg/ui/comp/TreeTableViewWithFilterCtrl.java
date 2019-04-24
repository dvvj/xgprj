package org.xg.ui.comp;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.utils.JFXHighlighter;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.xg.ui.utils.ExtendedAnimatedFlowContainer;
import org.xg.ui.utils.Global;
import org.xg.ui.utils.Helpers;
import org.xg.ui.utils.UIHelpers;

import java.util.List;
import java.util.Set;
import java.util.function.*;

import static io.datafx.controller.flow.container.ContainerAnimations.SWIPE_LEFT;

public class TreeTableViewWithFilterCtrl<T extends RecursiveTreeObject<T>> {

  private JFXHighlighter highlighter = new JFXHighlighter();

  private String emptyTableRes;

  private ObjectProperty<T> selected = new SimpleObjectProperty<>();

  public ObjectProperty<T> getSelected() {
    return selected;
  }

  @FXMLViewFlowContext
  private ViewFlowContext context;

  public void setup(
    String refreshRes,
    String searchRes,
    String filterRes,
    String emptyTableRes,
    Function<T, Set<String>> searchStringCollector,
    Runnable runRefresh
  ) throws Exception {
    initDrawer();

    this.emptyTableRes = emptyTableRes;

    txtSearch.setPromptText(Global.AllRes.getString(searchRes));
    btnRefresh.setText(Global.AllRes.getString(refreshRes));
    btnRefresh.setOnAction(e -> {
      runRefresh.run();
    });

    txtSearch.setOnKeyReleased(e -> {
//      filterAndUpdateTable2(
//        null,
//        t -> {
//          Set<String> strs = searchStringCollector.apply((T)t);
//          return strs.stream().anyMatch(s -> s.toLowerCase().contains(txtSearch.getText()));
//        }
//      );
      filterExisting(
        t -> {
          Set<String> strs = searchStringCollector.apply((T)t);
          return strs.stream().anyMatch(s -> s.toLowerCase().contains(txtSearch.getText()));
        }
      );
    });

  }

  public void setup(
    String refreshRes,
    String searchRes,
    String filterRes,
    String emptyTableRes,
    Function<T, Set<String>> searchStringCollector
  ) throws Exception {
    setup(refreshRes, searchRes, filterRes, emptyTableRes, searchStringCollector, () -> {});
  }


//  public void setup(
//    String refreshRes,
//    String searchRes,
//    String filterRes,
//    String emptyTableRes,
//    Function<T, Set<String>> searchStringCollector
//  ) throws Exception {
//    initDrawer();
//
//    this.emptyTableRes = emptyTableRes;
//
//    txtSearch.setPromptText(Global.AllRes.getString(searchRes));
//    btnRefresh.setText(Global.AllRes.getString(refreshRes));
//    btnRefresh.setOnAction(e -> {
//      Global.loggingTodo("[todo] refresh button pressed");
//    });
//
//    txtSearch.setOnKeyReleased(e -> {
//      filterExisting(
//        t -> {
//          Set<String> strs = searchStringCollector.apply((T)t);
//          return strs.stream().anyMatch(s -> s.toLowerCase().contains(txtSearch.getText()));
//        }
//      );
////      filterAndUpdateTable2(
////        null,
////        t -> {
////          Set<String> strs = searchStringCollector.apply((T)t);
////          return strs.stream().anyMatch(s -> s.toLowerCase().contains(txtSearch.getText()));
////        }
////      );
//    });
//
//  }

//  public void filterAndUpdateTable2(ObservableList<T> updatedData, Predicate<T> filter) {
//    Task<Integer> task = Helpers.uiTaskJ(
//      () -> {
//        return 0;
//      },
//      resp -> {
//        updateAndFilter(updatedData, filter);
//        //highlighter.highlight(theTable, txtSearch.getText());
//        return null;
//      },
//      1000
//    );
//    new Thread(task).start();
//  }
//
//  public void filterAndUpdateTable2(Predicate<T> filter) {
//    filterAndUpdateTable2(null, filter);
//  }

  private ObservableList<T> dataCache;
  //private ObservableList<T> activeDataCache;
  private SimpleObjectProperty<TreeItem<T>> theRoot = new SimpleObjectProperty<>();
  public void updateActiveData(ObservableList<T> activeData) {
    TreeItem<T> tr = new RecursiveTreeItem<T>(activeData, RecursiveTreeObject::getChildren);
    theRoot.setValue(tr);
  }

  public void setupColumsAndLoadData(
    Consumer<JFXTreeTableView<T>> columnBuilder,
    Supplier<ObservableList<T>> dataRetriever
  ) {
    setupColumsAndLoadData(
      columnBuilder,
      dataRetriever,
      () -> { }
    );
  }

  public void setupColumsAndLoadData(
    Consumer<JFXTreeTableView<T>> columnBuilder,
    Supplier<ObservableList<T>> dataRetriever,
    Runnable uiUpdater
  ) {
    columnBuilder.accept(theTable);

    //theTable.reGroup();
    theTable.rootProperty().bind(theRoot);
    theTable.setShowRoot(false);
    theTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      //System.out.println(newValue.getValue());
      if (newValue != null)
        selected.setValue(newValue.getValue());
    });

    Task<ObservableList<T>> fetchCustomersTask = Helpers.uiTaskJ(
      () -> {
        try {

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
          uiUpdater.run();
          updateAndFilter(resp, o -> true);
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

//  private void filterAndUpdateTable(ObservableList<T> data) {
//    filterAndUpdateTable(data, t -> true);
//  }
//
//  private void filterAndUpdateTable(Predicate<T> filter) {
//    filterAndUpdateTable(null, filter);
//  }

//  private void filterAndUpdateTable(ObservableList<T> data, Predicate<T> filter) {
//    if (data != null)
//      dataCache = data;
//    if (dataCache != null && dataCache.size() > 0) {
//      highlighter.clear();
//      ObservableList<T> activeData = dataCache.filtered(filter);
//      updateActiveData(activeData);
//      theTable.rootProperty().bind(theRoot);
//
//      //UIHelpers.setRoot4TreeView(theTable, activeDataCache);
//      Global.loggingTodo(String.format("found %d entries", activeData.size()));
//      //tblProducts.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener(productsCache));
//      if (activeData.size() > 0) {
//        TreeItem<T> first =  theTable.getTreeItem(0);
//        theTable.getSelectionModel().select(first);
//        selected.setValue(first.getValue());
//        //updateSelection(first.getValue());
//      }
//      else {
//        UIHelpers.setPlaceHolder4EmptyTreeView(theTable, emptyTableRes);
//        selected.setValue(null);
//      }
//
//    }
//    else {
//      UIHelpers.setPlaceHolder4EmptyTreeView(theTable, emptyTableRes);
//      selected.setValue(null);
//    }
//
//    theTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//      //System.out.println(newValue.getValue());
//      if (newValue != null)
//        selected.setValue(newValue.getValue());
//    });
//  }

  public void updateDataNoFilter(Supplier<ObservableList<T>> dataUpdater) {
    updateDataAndFilter(dataUpdater, t -> true);
  }

  public void updateDataAndFilter(
    Supplier<ObservableList<T>> dataUpdater,
    Predicate<T> filter
  ) {
    Task<ObservableList<T>> task = Helpers.uiTaskJ(
      dataUpdater::get,
      updatedData -> {
        updateAndFilter(updatedData, filter);
        //filterAndUpdateTable(updatedData, filter);
        //highlighter.highlight(theTable, txtSearch.getText());
        return null;
      },
      1000
    );
    new Thread(task).start();
  }

  private void updateAndFilter(ObservableList<T> updatedData, Predicate<T> filter) {
    dataCache = updatedData;
    if (dataCache != null && dataCache.size() > 0) {
      highlighter.clear();
      ObservableList<T> activeData = dataCache.filtered(filter);
      updateActiveData(activeData);
//      theTable.rootProperty().bind(theRoot);
//      UIHelpers.setRoot4TreeView(theTable, activeDataCache);
      Global.loggingTodo(String.format("Filter: found %d entries", activeData.size()));
      //tblProducts.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener(productsCache));
      if (activeData.size() > 0) {
        TreeItem<T> first =  theTable.getTreeItem(0);
        theTable.getSelectionModel().select(first);
        selected.setValue(first.getValue());
        //Global.loggingTodo(String.format("selected: %s", first.getValue().toString()));
        //updateSelection(first.getValue());
      }
      else {
        UIHelpers.setPlaceHolder4EmptyTreeView(theTable, emptyTableRes);
        selected.setValue(null);
      }

    }
    else {
      UIHelpers.setPlaceHolder4EmptyTreeView(theTable, emptyTableRes);
      selected.setValue(null);
    }
  }

  public void filterExisting(Predicate<T> filter) {
    if (dataCache != null && dataCache.size() > 0) {
      highlighter.clear();
      ObservableList<T> activeData = dataCache.filtered(filter);
      updateActiveData(activeData);
//      theTable.rootProperty().bind(theRoot);
//      UIHelpers.setRoot4TreeView(theTable, activeDataCache);
      Global.loggingTodo(String.format("Filter: found %d entries", activeData.size()));
      //tblProducts.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener(productsCache));
      if (activeData.size() > 0) {
        TreeItem<T> first =  theTable.getTreeItem(0);
        theTable.getSelectionModel().select(first);
        selected.setValue(first.getValue());
        //updateSelection(first.getValue());
      }
      else {
        UIHelpers.setPlaceHolder4EmptyTreeView(theTable, emptyTableRes);
        selected.setValue(null);
      }

    }
    else {
      UIHelpers.setPlaceHolder4EmptyTreeView(theTable, emptyTableRes);
      selected.setValue(null);
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

//  @FXML
//  private JFXDrawer drawer;
//  @FXML
//  private StackPane titleBurgerContainer;
//  @FXML
//  private JFXHamburger titleBurger;
  private void initDrawer() throws Exception {

//    theTable = new JFXTreeTableView<>();
//
//    drawer.setOnDrawerOpening(e -> {
//      final Transition animation = titleBurger.getAnimation();
//      animation.setRate(1);
//      animation.play();
//    });
//    drawer.setOnDrawerClosing(e -> {
//      final Transition animation = titleBurger.getAnimation();
//      animation.setRate(-1);
//      animation.play();
//    });
//    titleBurgerContainer.setOnMouseClicked(e -> {
//      if (drawer.isClosed() || drawer.isClosing()) {
//        drawer.open();
//      } else {
//        drawer.close();
//      }
//    });
//
//    context = new ViewFlowContext();
//    Flow innerFlow = new Flow(JFXTreeTableViewCtrl.class);
//
//    final FlowHandler flowHandler = innerFlow.createHandler(context);
//    context.register("ContentFlowHandler", flowHandler);
//    context.register("ContentFlow", innerFlow);
//    final Duration containerAnimationDuration = Duration.millis(320);
//    drawer.setContent(flowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration, SWIPE_LEFT)));
//    context.register("ContentPane", drawer.getContent().get(0));
//
//    Flow sideMenuFlow = new Flow(OrgAgentSideMenuCtrl.class);
//    final FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(context);
//    drawer.setSidePane(sideMenuFlowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration,
//      SWIPE_LEFT)));
  }

  @FXML
  private HBox exComps;
  public void addExtraComponents(Node... components) {
    exComps.getChildren().addAll(components);
  }
  public void addExtraComponents(List<Node> components) {
    exComps.getChildren().addAll(components);
  }
}
