package org.xg.hbn.ent;

import javax.persistence.*;

@Entity
@Table(name="product_assets")
public class Asset {
  @Id
  @Column(name="product_id")
  private Integer productId;

  @Column(name="assets", length = 8191, nullable = false)
  private String assetJson;

  public Asset() { }

  public Asset(Integer productId, String assetJson) {
    this.productId = productId;
    this.assetJson = assetJson;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public String getAssetJson() {
    return assetJson;
  }

  public void setAssetJson(String assetJson) {
    this.assetJson = assetJson;
  }
}
