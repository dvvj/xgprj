package org.xg.hbn.ent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reward_plans")
public class RewardPlan {
  @Id
  @Column(name="id", length = 31, nullable = false)
  private String id;

  @Column(name="info", length = 4095, nullable = false)
  private String info;

  @Column(name="defi", length = 8191, nullable = false)
  private String defi;

  @Column(name="vtag", length = 31, nullable = false)
  private String vtag;

  public RewardPlan() { }

  public RewardPlan(
    String id,
    String info,
    String defi,
    String vtag
  ) {
    this.id = id;
    this.info = info;
    this.defi = defi;
    this.vtag = vtag;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getDefi() {
    return defi;
  }

  public void setDefi(String defi) {
    this.defi = defi;
  }

  public String getVtag() {
    return vtag;
  }

  public void setVtag(String vtag) {
    this.vtag = vtag;
  }

}
