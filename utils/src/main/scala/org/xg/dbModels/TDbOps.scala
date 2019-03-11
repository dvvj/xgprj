package org.xg.dbModels

trait TDbOps {
  def addNewCustomer(
    uid:String,
    name:String,
    pass:String,
    idCardNo:String,
    mobile:String,
    postalAddr:String,
    refUid: String,
    bday:String
  ):String
  def allCustomers:Array[MCustomer]
  def customersRefedBy(refUid:String):Array[MCustomer]

  def allPricePlans:Array[MPricePlan]

  // order related
  def ordersOf(uid:String):Array[MOrder]
  def placeOrder(uid:String, productId:Int, qty:Double):Long
  def updateOrder(orderId:Long, newQty:Double):Boolean
  def setOrderPayTime(orderId:Long):Boolean

  def testAllOrderHistory:Array[MOrderHistory]
  def lockOrders:Array[MOrder]

  // product related
  def allProducts:Array[MProduct]

  // profs related
  def addNewMedProf(
    profId:String,
    name:String,
    pass:String,
    idCardNo:String,
    mobile:String
  ):String
  def allMedProfs:Array[MMedProf]
  def customersOf(profId:String):Array[MCustomer]

  // authentication related
  def getUserPassMap:Map[String, Array[Byte]]
  def getMedProfPassMap: Map[String, Array[Byte]]

  import collection.JavaConverters._
  def getUserPassMapJ:java.util.Map[String, Array[Byte]] = {
    getUserPassMap.asJava
  }
  def getMedProfPassMapJ:java.util.Map[String, Array[Byte]] = {
    getMedProfPassMap.asJava
  }
}