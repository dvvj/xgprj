package org.xg.hbn.ent;

import javax.persistence.*;

@Entity
@Table(name="alipay_notify_raw")
public class AlipayNotifyRaw {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="order_id")
  private Long orderId;

  @Column(name="trade_dt", length = 31)
  private String tradeDt;

  @Column(name="raw", length = 4095)
  private String raw;

  public AlipayNotifyRaw() { }

  public AlipayNotifyRaw(
    Long orderId,
    String tradeDt,
    String raw
  ) {
    this.orderId = orderId;
    this.tradeDt = tradeDt;
    this.raw = raw;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getTradeDt() {
    return tradeDt;
  }

  public void setTradeDt(String tradeDt) {
    this.tradeDt = tradeDt;
  }

  public String getRaw() {
    return raw;
  }

  public void setRaw(String raw) {
    this.raw = raw;
  }
}
