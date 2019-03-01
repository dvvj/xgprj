package org.xg.db.model

case class MOrderHistory(
  orderId:Long,
  updateTime:String,
  oldQty:Double
)
