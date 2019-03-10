package org.xg.dbModels

case class MOrderHistory(
  orderId:Long,
  updateTime:String,
  oldQty:Double
)
