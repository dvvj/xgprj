package org.xg.hbn.ent;

import javax.persistence.*;

@Entity
@Table(name="customer_profiles")
public class CustomerProfile {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="prof_id", length = 255, nullable = false)
  private String profId;

  @Column(name="customer_id", length = 255, nullable = false)
  private String customerId;

  @Column(name="detailed_info", length = 8191, nullable = false)
  private String detailedInfo;

  @Column(name="version", length = 31, nullable = false)
  private String version;

  public CustomerProfile() { }
  public CustomerProfile(
    String profId,
    String customerId,
    String detailedInfo,
    String version
  ) {
    this.profId = profId;
    this.customerId = customerId;
    this.detailedInfo = detailedInfo;
    this.version = version;
  }

  public Long getId() {
    return id;
  }

  public String getProfId() {
    return profId;
  }

  public void setProfId(String profId) {
    this.profId = profId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getDetailedInfo() {
    return detailedInfo;
  }

  public void setDetailedInfo(String detailedInfo) {
    this.detailedInfo = detailedInfo;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
}
