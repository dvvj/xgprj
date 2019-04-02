package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MProfOrgAgent;
import org.xg.uiModels.ProfOrgAgent;

import java.util.Arrays;

public class MedProfOrgDataModel {
  private final ObservableList<ProfOrgAgent> agents;

  public MedProfOrgDataModel(
    MProfOrgAgent[] agents
  ) {
    this.agents = FXCollections.observableArrayList(
      Arrays.stream(agents).map(ProfOrgAgent::fromM).toArray(ProfOrgAgent[]::new)
    );
  }

  public ObservableList<ProfOrgAgent> getAgents() {
    return agents;
  }
}
