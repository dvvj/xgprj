package org.xg.ui.mainwnd;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import io.datafx.controller.ViewConfiguration;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.xg.ui.utils.Global;

import java.nio.charset.StandardCharsets;

public class MainFrame {

  @FXMLViewFlowContext
  private ViewFlowContext flowContext;

  public void start(Class<?> clazz) {
    start(null, clazz);
  }

  private final String titleResKey;
  public MainFrame(String titleResKey) {
    this.titleResKey = titleResKey;
  }

  public void start(Stage stage, Class<?> clazz) {
//    new Thread(() -> {
//      try {
//        SVGGlyphLoader.loadGlyphsFont(MainDemo.class.getResourceAsStream("/fonts/icomoon.svg"),
//          "icomoon.svg");
//      } catch (IOException ioExc) {
//        ioExc.printStackTrace();
//      }
//    }).start();
    try {
      if (stage == null)
        stage = new Stage();
      ViewConfiguration viewCfg = new ViewConfiguration();
      viewCfg.setResources(Global.AllRes);
      viewCfg.setCharset(StandardCharsets.UTF_8);
      Flow flow = new Flow(clazz, viewCfg);
      DefaultFlowContainer container = new DefaultFlowContainer();
      flowContext = new ViewFlowContext();
      flowContext.register("Stage", stage);

      FlowHandler flowHandler = new FlowHandler(flow, flowContext, viewCfg);
      flowHandler.start(container);
      //flow.createHandler(flowContext).start(container);

      JFXDecorator decorator = new JFXDecorator(stage, container.getView(), false, false, false);
      decorator.setCustomMaximize(false);
      decorator.setGraphic(new SVGGlyph(""));

      stage.setTitle(Global.AllRes.getString(titleResKey));

//      double width = 800;
//      double height = 600;
//      try {
//        Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
//        width = bounds.getWidth() / 2.5;
//        height = bounds.getHeight() / 1.35;
//      }catch (Exception e){ }
//      Scene scene = new Scene(decorator, width, height);

      Scene scene = new Scene(decorator);
      stage.setResizable(false);

      final ObservableList<String> stylesheets = scene.getStylesheets();
      stylesheets.addAll(
        //MainDemo.class.getResource("/css/jfoenix-fonts.css").toExternalForm(),
        //MainDemo.class.getResource("/css/jfoenix-design.css").toExternalForm(),
        getClass().getResource("/default.css").toExternalForm(),
        getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm());
      stage.setScene(scene);
      stage.show();
    }
    catch (Exception ex) {
      throw new RuntimeException("Error launching window", ex);
    }
  }
}
