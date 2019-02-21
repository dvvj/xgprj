package org.xg.ui.model;

import java.util.List;

public class Product {
  public int id;
  public String name;
  public double price0; //todo
  public String detailedInfo;
  public List<String> keywords;

  public Product() { }
  public Product(int id, String name, double price0, String detailedInfo, List<String> keywords) {
    this.id = id;
    this.name = name;
    this.price0 = price0;
    this.detailedInfo = detailedInfo;
    this.keywords = keywords;
  }
}
