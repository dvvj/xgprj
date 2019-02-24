package org.xg.ui.model;

import org.xg.db.model.MProduct;

import java.util.Arrays;
import java.util.List;

public class Product {
  private Integer id;
  private String name;

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

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  private Double price0; //todo
  private String detailedInfo;
  private List<String> keywords;

  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }
  public void setId(String name) {
    this.name = name;
  }

  public Product() { }
  public Product(int id, String name, double price0, String detailedInfo, List<String> keywords) {
    this.id = id;
    this.name = name;
    this.price0 = price0;
    this.detailedInfo = detailedInfo;
    this.keywords = keywords;
  }

  public static Product fromMProduct(MProduct mp) {
    double roundPrice = Math.round(mp.price0() * 100) / 100.0;
    return new Product(
      mp.id(),
      mp.name(),
      roundPrice,
      mp.detailedInfo(),
      Arrays.asList(mp.keywordsArr())
    );
  }
}
