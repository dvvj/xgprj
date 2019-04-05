package org.xg.hbn.ent;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="reward_plan_map")
public class RewardPlanMap {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="uid", length = 255, nullable = false)
  private String uid;

  @Column(name="plan_ids", length = 255, nullable = false)
  private String planIds;

  @Column(name="start_time", nullable = false)
  private ZonedDateTime startTime;

  @Column(name="expire_time")
  private ZonedDateTime expireTime;

  public RewardPlanMap() { }

  public RewardPlanMap(
    String uid,
    String planIds,
    ZonedDateTime startTime,
    ZonedDateTime expireTime
  ) {
    this.uid = uid;
    this.planIds = planIds;
    this.startTime = startTime;
    this.expireTime = expireTime;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getPlanIds() {
    return planIds;
  }

  public void setPlanIds(String planIds) {
    this.planIds = planIds;
  }

  public ZonedDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(ZonedDateTime startTime) {
    this.startTime = startTime;
  }

  public ZonedDateTime getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(ZonedDateTime expireTime) {
    this.expireTime = expireTime;
  }

  public Long getId() {
    return id;
  }

}
