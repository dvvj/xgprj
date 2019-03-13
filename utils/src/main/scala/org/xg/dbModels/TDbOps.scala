package org.xg.dbModels

import org.xg.busiLogic.{PricePlanLogics, RewardPlanLogics}

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
  import org.xg.gnl.DataUtils._
  import collection.JavaConverters._

  def allCustomers:Array[MCustomer]
  def allCustomersToMap:Map[String, MCustomer] = {
    val all = allCustomers
    all.map(p => p.uid -> p).toMap
  }
  def allCustomersToMapJ:java.util.Map[String, MCustomer] = {
    allCustomersToMap.asJava
  }
  def customersRefedBy(refUid:String):Array[MCustomer]

  def allPricePlans:Array[MPricePlan]
  def allPricePlansToMap:Map[String, MPricePlan] = {
    val all = allPricePlans
    all.map(p => p.id -> p).toMap
  }
  def allPricePlansToMapJ:java.util.Map[String, MPricePlan] = {
    allPricePlansToMap.asJava
  }
  def pricePlansByUid(uid:String):Array[MPricePlanMap]
  def allPricePlanMaps:Array[MPricePlanMap]
  def allActivePricePlans:Map[String, MPricePlanMap] = {
    val all = allPricePlanMaps
    PricePlanLogics.activePricePlans(all)
  }
  def allActivePricePlansJ:java.util.Map[String, MPricePlanMap] = {
    allActivePricePlans.asJava
  }

  def allRewardPlans: Array[MRewardPlan]
  def allRewardPlanMaps:Array[MRewardPlanMap]
  def allActiveRewardPlans:Map[String, MRewardPlanMap] = {
    val all = allRewardPlanMaps
    RewardPlanLogics.activeRewardPlans(all)
  }

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