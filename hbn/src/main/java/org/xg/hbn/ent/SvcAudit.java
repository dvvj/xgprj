package org.xg.hbn.ent;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="svc_audit")
public class SvcAudit {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="ops", length = 127, nullable = false)
  private String ops;

  @Column(name="ts", nullable = false)
  private ZonedDateTime ts;

  @Column(name="status", nullable = false)
  private Integer status;

  @Column(name="duration", nullable = false)
  private Integer duration;

  @Column(name="uid", length = 255)
  private String uid;

  @Column(name="extra", length = 1023)
  private String extra;

  public SvcAudit() { }

  public SvcAudit(
    String ops,
    ZonedDateTime ts,
    Integer status,
    Integer duration,
    String uid,
    String extra
  ) {
    this.ops = ops;
    this.ts = ts;
    this.status = status;
    this.duration = duration;
    this.uid = uid;
    this.extra = extra;
  }

  public SvcAudit(
    String ops,
    ZonedDateTime ts,
    Integer status,
    Integer duration,
    String uid
  ) {
    this(ops, ts, status, duration, uid, null);
  }

  public Long getId() {
    return id;
  }

  public String getOps() {
    return ops;
  }

  public void setOps(String ops) {
    this.ops = ops;
  }

  public ZonedDateTime getTs() {
    return ts;
  }

  public void setTs(ZonedDateTime ts) {
    this.ts = ts;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getExtra() {
    return extra;
  }

  public void setExtra(String extra) {
    this.extra = extra;
  }
}
