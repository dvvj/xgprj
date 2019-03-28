package org.xg.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.xg.dbModels.MMedProf;
import org.xg.dbModels.MOrgOrderStat;
import org.xg.ui.utils.Helpers;
import org.xg.uiModels.MedProf;
import org.xg.uiModels.OrgOrderStat;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProfOrgsDataModel {

  private final ObservableList<MedProf> medProfs;
  private final OrgOrderStat[] rawOrderStats;
  private final ObservableList<OrgOrderStat> orderStats;
  private final Map<String, MedProf> profMap;

  public ProfOrgsDataModel(
    MMedProf[] medProfs,
    MOrgOrderStat[] orderStats
  ) {
    MedProf[] profs = Helpers.convMedProfs(medProfs);
    profMap = Arrays.stream(profs).collect(
      Collectors.toMap(
        MedProf::getUid, Function.identity()
      )
    );
    this.medProfs = FXCollections.observableArrayList(profs);
    rawOrderStats = Helpers.convOrgOrderStats(orderStats, profMap);
    this.orderStats = FXCollections.observableArrayList(rawOrderStats);
  }

  public ObservableList<MedProf> getMedProfs() {
    return medProfs;
  }

  public ObservableList<OrgOrderStat> getOrderStats() {
    return orderStats;
  }

  public OrgOrderStat[] getRawOrderStats() {
    return rawOrderStats;
  }
}
