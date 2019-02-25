package org.xg.hbn.ent;

import javax.persistence.*;

@Entity
@Table(name="products", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Product {

  private Integer id;
  private String name;
  private Double price0;
  private String detailedInfo;
  private String keywords;

  public Product() { }

  public Product(
    Integer id,
    String name,
    Double price0,
    String detailedInfo,
    String keywords
  ) {
    this.id = id;
    this.name = name;
    this.price0 = price0;
    this.detailedInfo = detailedInfo;
    this.keywords = keywords;
  }

  @Id
  @Column(name="id")
  public Integer getId() {
    return id;
  }

  @Column(name="name", length = 63, nullable = false)
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  @Column(name="price0", nullable = false)
  public Double getPrice0() {
    return price0;
  }
  public void setPrice0(Double price0) {
    this.price0 = price0;
  }

  @Column(name="detailed_info", length = 511)
  public String getDetailedInfo() {
    return detailedInfo;
  }
  public void setDetailedInfo(String detailedInfo) {
    this.detailedInfo = detailedInfo;
  }

  @Column(name="keywords", length = 255)
  public String getKeywords() {
    return keywords;
  }
  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }
}
