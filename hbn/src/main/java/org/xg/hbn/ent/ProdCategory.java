package org.xg.hbn.ent;

import javax.persistence.*;

@Entity
@Table(name="prod_categories", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class ProdCategory {
  @Id
  @Column(name="id")
  private Long id;

  @Column(name="name", length = 1023, nullable = false)
  private String name;

  @Column(name="detailed_info", length = 8191)
  private String detailedInfo;

  public ProdCategory() { }

  public ProdCategory(
    Long id,
    String name,
    String detailedInfo
  ) {
    this.id = id;
    this.name = name;
    this.detailedInfo = detailedInfo;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDetailedInfo() {
    return detailedInfo;
  }

  public void setDetailedInfo(String detailedInfo) {
    this.detailedInfo = detailedInfo;
  }
}
