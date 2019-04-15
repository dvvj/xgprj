package org.xg.hbn.ent;

import javax.persistence.*;

@Entity
@Table(name="products", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Product {

  @Id
  @Column(name="id")
  private Integer id;

  @Column(name="name", length = 1023, nullable = false)
  private String name;
  @Column(name="price0", nullable = false)
  private Double price0;
  @Column(name="detailed_info", length = 8191)
  private String detailedInfo;
  @Column(name="keywords", length = 511)
  private String keywords;
  @Column(name="categories", length = 1023)
  private String categories;

  public Product() { }

  public Product(
    Integer id,
    String name,
    Double price0,
    String detailedInfo,
    String keywords,
    String categories
  ) {
    this.id = id;
    this.name = name;
    this.price0 = price0;
    this.detailedInfo = detailedInfo;
    this.keywords = keywords;
    this.categories = categories;
  }


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public Double getPrice0() {
    return price0;
  }
  public void setPrice0(Double price0) {
    this.price0 = price0;
  }

  public String getDetailedInfo() {
    return detailedInfo;
  }
  public void setDetailedInfo(String detailedInfo) {
    this.detailedInfo = detailedInfo;
  }

  public String getKeywords() {
    return keywords;
  }
  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public String getCategories() {
    return categories;
  }

  public void setCategories(String categories) {
    this.categories = categories;
  }
}