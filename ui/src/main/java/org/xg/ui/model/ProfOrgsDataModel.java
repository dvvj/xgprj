package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MMedProf;
import org.xg.ui.utils.Helpers;
import org.xg.uiModels.MedProf;

public class ProfOrgsDataModel {

  private final ObservableList<MedProf> medProfs;

  public ProfOrgsDataModel(
    MMedProf[] medProfs
  ) {
    this.medProfs = FXCollections.observableArrayList(Helpers.convMedProfs(medProfs));
  }

  public ObservableList<MedProf> getMedProfs() {
    return medProfs;
  }
}
