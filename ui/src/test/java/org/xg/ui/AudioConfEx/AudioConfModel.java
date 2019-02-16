package org.xg.ui.AudioConfEx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SingleSelectionModel;


public class AudioConfModel {

  public double minDecibels = 0.0;
  public double maxDecibels = 160.0;

  public IntegerProperty selectedDBs = new SimpleIntegerProperty(0);

  public BooleanProperty muted = new SimpleBooleanProperty(false);

  public ObservableList<String> genres = FXCollections.observableArrayList(
    "Chamber",
    "Country",
    "Cowbell",
    "Metal",
    "Polka",
    "Rock"
  );

  public SingleSelectionModel genreSelection;

  public void addListener2GenreSelection() {
    genreSelection.selectedIndexProperty().addListener(obs -> {
      int selectedIndex = genreSelection.selectedIndexProperty().getValue();
      System.out.println("selectedDBs: " + selectedDBs);
      switch (selectedIndex) {
        case 0:
          selectedDBs.setValue(80);
          break;
        case 1:
          selectedDBs.setValue(100);
          break;
        case 2:
          selectedDBs.setValue(150);
          break;
        case 3:
          selectedDBs.setValue(140);
          break;
        case 4:
          selectedDBs.setValue(120);
          break;
        case 5:
          selectedDBs.setValue(130);
          break;
        default:
          throw new IllegalArgumentException("Unknown selectedIndex: " + selectedIndex);
      }
    });
  }
}
