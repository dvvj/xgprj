package org.xg.ui.model;

import org.xg.dbModels.*;
import org.xg.pay.pricePlan.TPricePlan;

import java.util.Arrays;
import java.util.List;

public class Product {
  private Integer id;
  private String name;
  private Double price0; //todo
  private TPricePlan pricePlan;
  private String detailedInfo;
  private List<String> keywords;

  public Double getPrice0() {
    return price0;
  }

  public Double getActualPrice() {
    return pricePlan.adjust(getId(), getPrice0());
  }

  public String getPriceDetail() {
    if (pricePlan != null) {
      Double actualPrice = getActualPrice();
      Double discount = (1 - actualPrice / getPrice0())*100;
      return String.format(
        "%.2f (省%d%%)", actualPrice, discount.intValue()
      );
    }
    else {
      return getPrice0().toString();
    }
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


  public List<AssetItem> getAssets() {
    return assets;
  }

  public void setAssets(List<AssetItem> assets) {
    this.assets = assets;
  }

  private List<AssetItem> assets;

  public Product() { }
  public Product(
    int id, String name, double price0,
    TPricePlan pricePlan,
    String detailedInfo,
    List<String> keywords, List<AssetItem> assets) {
    this.id = id;
    this.name = name;
    this.price0 = price0;
    this.pricePlan = pricePlan;
    this.detailedInfo = detailedInfo;
    this.keywords = keywords;
    this.assets = assets;
  }

  public static Product fromMProduct(MProduct mp, TPricePlan pricePlan) {
    double roundPrice = Math.round(mp.price0() * 100) / 100.0;
    return new Product(
      mp.id(),
      mp.name(),
      roundPrice,
      pricePlan,
      mp.detailedInfo(),
      Arrays.asList(mp.keywordsArr()),
      Arrays.asList(mp.assets().assets())
    );
  }
}
