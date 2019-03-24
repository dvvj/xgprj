package org.xg.ui.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.xg.dbModels.*;
import org.xg.gnl.DataUtils;
import org.xg.pay.pricePlan.TPricePlan;

import java.util.Arrays;
import java.util.List;

public class Product extends RecursiveTreeObject<Product> {
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
    Detail detail,
    List<String> keywords, List<AssetItem> assets) {
    this.id = id;
    this.name = name;
    this.price0 = price0;
    this.pricePlan = pricePlan;
    this.detailedInfo = detailedInfo;
    this.detail = detail;
    this.keywords = keywords;
    this.assets = assets;
  }

  public static Product fromMProduct(MProduct mp, TPricePlan pricePlan) {
    double roundPrice = DataUtils.roundMoney(mp.price0()); //Math.round(mp.price0() * 100) / 100.0;
    return new Product(
      mp.id(),
      mp.name(),
      roundPrice,
      pricePlan,
      mp.detailedInfo(),
      Detail.from(mp.prodDetail()),
      Arrays.asList(mp.keywordsArr()),
      Arrays.asList(mp.assets().assets())
    );
  }

  public static class Detail {
    private String srcCountry;
    private String desc;

    public Detail(String srcCountry, String desc) {
      this.srcCountry = srcCountry;
      this.desc = desc;
    }

    public String getSrcCountry() {
      return srcCountry;
    }

    public void setSrcCountry(String srcCountry) {
      this.srcCountry = srcCountry;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public static Detail from(MProdDetail mpd) {
      return new Detail(mpd.srcCountry(), mpd.desc());
    }
  }

  private Detail detail;

  public Detail getDetail() {
    return detail;
  }

  public void setDetail(Detail detail) {
    this.detail = detail;
  }

}
