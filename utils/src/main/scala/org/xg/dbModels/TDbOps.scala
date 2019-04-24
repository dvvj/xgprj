package org.xg.dbModels

import java.time.{Period, ZonedDateTime}
import java.time.temporal.ChronoUnit

import org.xg.busiLogic.{OrderStatusLogics, PricePlanLogics, RewardPlanLogics}
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
  def ordersOf_Unpaid(uid:String):Array[MOrder] = {
    ordersOf(uid).filter(OrderStatusLogics.isUnpaid)
  }
  def ordersOf_Cancelled(uid:String):Array[MOrder] = {
    ordersOf(uid).filter(OrderStatusLogics.isCancelled)
  }

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
  def placeOrder(uid:String, refUid:String, orgAgentId:String, productId:Int, qty:Double, actualCost:Double):Long
//  def updateOrder(orderId:Long, newQty:Double):Boolean
  def cancelOrder(orderId: Long): Boolean
  def saveAlipayNotifyRawAndPayOrder(notifyRaw:String, orderId:Long, tradeDt:String):Boolean
  def saveAlipayNotifyRawButDoNotPay(notifyRaw:String):Boolean

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
    mobile:String,
    orgAgentId:String
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

  def allProfOrgAgents:Array[MProfOrgAgent]

  def profOrgAgentMap:Map[String, MProfOrgAgent] = {
    allProfOrgAgents.map(a => a.orgAgentId -> a).toMap
  }

  def profOrg2AgentMap:Map[String, Array[MProfOrgAgent]] = {
    allProfOrgAgents.groupBy(_.orgId)
  }
  def profOrg2AgentMapJ:java.util.Map[String, Array[MProfOrgAgent]] = {
    profOrg2AgentMap.asJava
  }

  def profOrgAgentMapJ:java.util.Map[String, MProfOrgAgent] = {
    profOrgAgentMap.asJava
  }

  def profsOf(profOrgAgentId:String):Array[MMedProf]
  def profMapOf(profOrgAgentId:String):Map[String, MMedProf] = {
    profsOf(profOrgAgentId).map(p => p.profId -> p).toMap
  }
  def profMapOfJ(profOrgAgentId:String):java.util.Map[String, MMedProf] = {
    profMapOf(profOrgAgentId).asJava
  }

  def medprofsByIds(profIds:Array[String]):Array[MMedProf]


  // authentication related
  def getUserPassMap:Map[String, Array[Byte]]
  def getMedProfPassMap: Map[String, Array[Byte]]
  def getProfOrgAgentPassMap: Map[String, Array[Byte]]
  def getMedProfOrgPassMap: Map[String, Array[Byte]]

  def getUserPassMapJ:java.util.Map[String, Array[Byte]] = {
    getUserPassMap.asJava
  }
  def getMedProfPassMapJ:java.util.Map[String, Array[Byte]] = {
    getMedProfPassMap.asJava
  }
  def getProfOrgAgentPassMapJ:java.util.Map[String, Array[Byte]] = {
    getProfOrgAgentPassMap.asJava
  }
  def getMedProfOrgPassMapJ:java.util.Map[String, Array[Byte]] = {
    getMedProfOrgPassMap.asJava
  }

  def getOrderStat4OrgAgent(orgAgentId:String):Array[MOrgAgentOrderStat]

  def addPricePlan(prPlan:MPricePlan):String

  def addRewardPlan(rwPlan:MRewardPlan):String

  def orderStatsOfOrg(orgId:String):Array[MOrgOrderStat]

  def addPricePlanMap(ppm: MPricePlanMap):Boolean

  def addRewardPlanMap(ppm: MRewardPlanMap):Boolean

  def updateCustomerPass(customerId:String, oldPassHash:Array[Byte], newPassHash:Array[Byte]):OpResp

  def getCustomerProfiles(uid:String):Array[MCustomerProfile]

  def svcAudit(
    ops:String,
    duration:Int,
    uid:String
  ):OpResp = {
    svcAudit_Status(ops, MSvcAudit.StatusOK, duration, uid)
  }

  def svcAudit_Status(
    ops:String,
    status:Int,
    duration:Int,
    uid:String
  ):OpResp = {
    svcAuditEx(ops, status, duration, uid, null)
  }

  def svcAuditEx(
    ops:String,
    status:Int,
    duration:Int,
    uid:String,
    extra:String
  ):OpResp

  def svcAudit_NoUid(
    ops:String,
    duration:Int
  ):OpResp = {
    svcAudit(ops, duration, null)
  }

  def allSvcAudit:Array[MSvcAudit]
}

object TDbOps {
  type OrderFilter = MOrder => Boolean

}