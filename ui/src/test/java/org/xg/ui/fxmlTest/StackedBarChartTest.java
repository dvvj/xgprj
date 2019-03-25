package org.xg.ui.fxmlTest;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Random;

public class StackedBarChartTest extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("JavaFX Chart Demo");
    StackPane pane = new StackPane();
    pane.getChildren().add(createStackedBarChart());
    stage.setScene(new Scene(pane, 400, 200));
    stage.show();
  }

  public ObservableList<XYChart.Series<String, Double>>
  getDummyChartData() {
    ObservableList<XYChart.Series<String, Double>> data =
      FXCollections.observableArrayList();
    Series<String, Double> as = new Series<>();
    Series<String, Double> bs = new Series<>();
    Series<String, Double> cs = new Series<>();
    Series<String, Double> ds = new Series<>();
    Series<String, Double> es = new Series<>();
    Series<String, Double> fs = new Series<>();
    as.setName("A-Series");
    bs.setName("B-Series");
    cs.setName("C-Series");
    ds.setName("D-Series");
    es.setName("E-Series");
    fs.setName("F-Series");

    Random r = new Random();

    for (int i = 1900; i < 2017; i += 10) {

      as.getData().add(new XYChart.Data<>
        (Integer.toString(i), r.nextDouble()));
      bs.getData().add(new XYChart.Data<>
        (Integer.toString(i), r.nextDouble()));
      cs.getData().add(new XYChart.Data<>
        (Integer.toString(i), r.nextDouble()));
      ds.getData().add(new XYChart.Data<>
        (Integer.toString(i), r.nextDouble()));
      es.getData().add(new XYChart.Data<>
        (Integer.toString(i), r.nextDouble()));
      fs.getData().add(new XYChart.Data<>
        (Integer.toString(i), r.nextDouble()));
    }
    data.addAll(as, bs, cs, ds, es, fs);
    return data;
  }

  public XYChart<CategoryAxis, NumberAxis>
  createStackedBarChart() {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    StackedBarChart sbc = new StackedBarChart<>(xAxis, yAxis);
    sbc.setData(getDummyChartData());
    sbc.setTitle("Stacked bar chart on random data");
    return sbc;
  }
}
