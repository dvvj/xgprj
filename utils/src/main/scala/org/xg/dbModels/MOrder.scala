package org.xg.dbModels

import org.xg.busiLogic.OrderStatusLogics

case class MOrder(
  id:Long,
  uid:String,
  productId:Int,
  qty:Double,
  actualCost:Double,
  creationTimeS:String,
  payTime:Option[String],
  procTime1S:Option[String], // time order being locked, i.e. cannot be modified
  procTime2S:Option[String],
  procTime3S:Option[String]
) {
//  def creationTime:ZonedDateTime = {
//
//  }
//  def this(
//            id:Long,
//            uid:String,
//            productId:Int,
//            qty:Double,
//            creationTimeS:String
//          ) = this(id, uid, productId, qty, creationTimeS, None, None, None, None)
//  def this(
//            id:Long,
//            uid:String,
//            productId:Int,
//            qty:Double,
//            creationTimeS:String,
//            payTime:String
//          ) = this(id, uid, productId, qty, creationTimeS, Option(payTime), None, None, None)

  //def canBeModified:Boolean = procTime1S == null || procTime1S.isEmpty
  def canBePaid:Boolean = OrderStatusLogics.orderCanBePaid(this)
  def canBeCancelled:Boolean = OrderStatusLogics.orderCanBeCancelled(this)
}

object MOrder {
  import org.xg.json.CommonUtils._
  import org.xg.gnl.DataUtils._
  def toJsons(customers:Array[MOrder]):String = _toJsons(customers)
  def fromJsons(j:String):Array[MOrder] = _fromJsons(j)

  def createJ(id:Long,
              uid:String,
              productId:Int,
              qty:Double,
              actualCost:Double,
              creationTimeS:String
             ):MOrder =
    MOrder(id, uid, productId, qty, actualCost, creationTimeS, None, None, None, None)

  def createJ(id:Long,
              uid:String,
              productId:Int,
              qty:Double,
              actualCost:Double,
              creationTimeS:String,
              payTime:String
             ):MOrder =
    MOrder(id, uid, productId, qty, actualCost, creationTimeS, Option(payTime), None, None, None)

  def createJ(id:Long,
              uid:String,
              productId:Int,
              qty:Double,
              actualCost:Double,
              creationTimeS:String,
              payTime:String,
              procTime1:String,
              procTime2:String,
              procTime3:String
             ):MOrder =
    MOrder(
      id, uid, productId, qty, actualCost,
      creationTimeS,
      noneIfNull(payTime),
      noneIfNull(procTime1),
      noneIfNull(procTime2),
      noneIfNull(procTime3)
    )

}