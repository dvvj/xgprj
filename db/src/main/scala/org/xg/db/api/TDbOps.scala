package org.xg.db.api

import java.sql.Connection
import java.time.LocalDateTime

import org.joda.time.DateTime

trait TDbOps {
  def addNewCustomer(uid:String, name:String, pass:String, idCardNo:String, mobile:String, postalAddr:String, ref_uid: String, bday:String):Boolean

  def allCustomers:String
  def ordersOf(uid:String):String
  def placeOrder(uid:String, productId:Int, qty:Double):Long
  def allProducts:String
  def getUserPassMap:Map[String, Array[Byte]]
  def getUserPassMapJ:java.util.Map[String, Array[Byte]] = {
    import collection.JavaConverters._
    getUserPassMap.asJava
  }
}
