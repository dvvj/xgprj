package org.xg.dbModels

import java.time.{Period, ZonedDateTime}
import java.time.temporal.ChronoUnit

import org.xg.busiLogic.{PricePlanLogics, RewardPlanLogics}
import org.xg.gnl.DataUtils

trait TDbOps {
  import TDbOps._
//  def filterOrders(orders:Array[MOrder], filter:OrderFilter) =
//    orders.filter(filter)

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
  def allRewardPlansToMap:Map[String, MRewardPlan] = {
    val all = allRewardPlans
    all.map(p => p.id -> p).toMap
  }
  def allRewardPlansToMapJ:java.util.Map[String, MRewardPlan] = {
    allRewardPlansToMap.asJava
  }
  def allRewardPlanMaps:Array[MRewardPlanMap]
  def allActiveRewardPlans:Map[String, MRewardPlanMap] = {
    val all = allRewardPlanMaps
    RewardPlanLogics.activeRewardPlans(all)
  }
  def allActiveRewardPPlans:Map[String, MRewardPlanMap] = {
    val all = allRewardPlanMaps
    RewardPlanLogics.activeRewardPlans(all)
  }
  def allActiveRewardPlansJ:java.util.Map[String, MRewardPlanMap] = {
    allActiveRewardPPlans.asJava
  }
  // order related
  def ordersOf(uid:String):Array[MOrder]
  def ordersOf_Unpaid(uid:String):Array[MOrder]

  def ordersOf_CreationTimeWithin(uid:String, days:Int):Array[MOrder]
  def ordersOf_CreatedThisMonth(uid:String):Array[MOrder] = {
    val days = DataUtils.utcTimeNow.getDayOfMonth - 1
    ordersOf_CreationTimeWithin(uid, days)
  }
  def ordersOf_CreatedLastMonth(uid:String):Array[MOrder] = {
    val lastMonthNextDay = DataUtils.utcTimeNow.minusMonths(1).plusDays(1)
    val days = Period.between(lastMonthNextDay.toLocalDate, DataUtils.utcTimeNow.toLocalDate)
      .getDays
    ordersOf_CreationTimeWithin(uid, days)
  }

  def ordersOfCustomers(customerIds:Array[String]):Array[MOrder]
  def ordersOfCustomers_CreationTimeWithin(customerIds:Array[String], days:Int):Array[MOrder]
  def ordersOfCustomers_CreatedThisMonth(customerIds:Array[String]):Array[MOrder] = {
    val days = DataUtils.utcTimeNow.getDayOfMonth - 1
    ordersOfCustomers_CreationTimeWithin(customerIds, days)
  }
  def ordersOfCustomers_CreatedLastMonth(customerIds:Array[String]):Array[MOrder] = {
    val lastMonthNextDay = DataUtils.utcTimeNow.minusMonths(1).plusDays(1)
    val days = Period.between(lastMonthNextDay.toLocalDate, DataUtils.utcTimeNow.toLocalDate)
      .getDays
    ordersOfCustomers_CreationTimeWithin(customerIds, days)
  }
  def ordersOfCustomers_Unpaid(customerIds:Array[String]):Array[MOrder]

  //  def ordersOfCustomers_CreationTimeWithin(customerIds:Array[String], days:Int):Array[MOrder]
  def placeOrder(uid:String, productId:Int, qty:Double, actualCost:Double):Long
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
  def medProfsMap:Map[String, MMedProf] = {
    val all = allMedProfs
    all.map(p => p.profId -> p).toMap
  }
  def medProfsMapJ:java.util.Map[String, MMedProf] = {
    medProfsMap.asJava
  }
  def customersOf(profId:String):Array[MCustomer]

  def allProfOrgs:Array[MProfOrg]

  def profOrgMap:Map[String, MProfOrg] = {
    allProfOrgs.map(po => po.orgId -> po).toMap
  }

  def profOrgMapJ:java.util.Map[String, MProfOrg] = {
    profOrgMap.asJava
  }

  // authentication related
  def getUserPassMap:Map[String, Array[Byte]]
  def getMedProfPassMap: Map[String, Array[Byte]]

  def getUserPassMapJ:java.util.Map[String, Array[Byte]] = {
    getUserPassMap.asJava
  }
  def getMedProfPassMapJ:java.util.Map[String, Array[Byte]] = {
    getMedProfPassMap.asJava
  }
}

object TDbOps {
  type OrderFilter = MOrder => Boolean

//  private val OrderFilter_CreatedThisMonth:OrderFilter = mo => {
//    val zdt = DataUtils.utcTimeFromStr(mo.creationTimeS)
//    val zdtNow = DataUtils.utcTimeNow
//    zdt.getYear == zdtNow.getYear && zdt.getMonthValue == zdtNow.getMonthValue
//  }
}