package org.xg.ui.utils

import javafx.collections.{FXCollections, ObservableList}
import org.xg.auth.SvcHelpers
import org.xg.busiLogic.PricePlanLogics
import org.xg.dbModels._
import org.xg.gnl.GlobalCfg
import org.xg.pay.pricePlan.TPricePlan
import org.xg.pay.rewardPlan.TRewardPlan
import org.xg.svc.{AddNewCustomer, AddNewMedProf, CustomerPricePlan, UpdatePassword}
import org.xg.uiModels.Order

object UISvcHelpers {

  var userCfg:UserCfg = null
  def setCfg(cfg:UserCfg):Unit = {
    userCfg = cfg
  }

  def serverCfg:GlobalCfg = GlobalCfg(
    userCfg.svcSvr,
    userCfg.timeOutMs,
    "N/A",
    "N/A"
  )

  def trySvcTF(
                action:() => Boolean
              ):Boolean = {
    try {
      action()
    }
    catch {
      case t:Throwable => {
        Global.loggingTodo(s"Error adding new med prof: ${t.getMessage}")
        false
      }
    }
  }

  def trySvc[T](
    action:() => T
  ):T = {
    try {
      action()
    }
    catch {
      case t:Throwable => {
        Global.loggingTodo(s"Error adding new med prof: ${t.getMessage}")
        throw t
      }
    }
  }

  def addNewMedProf(mp:AddNewMedProf):Boolean = {
    trySvcTF { () =>
      SvcHelpers.post(
        serverCfg.addNewMedProfURL,
        Global.getCurrToken,
        AddNewMedProf.toJson(mp)
      )
      true
    }
  }

  def addNewCustomer(nc:AddNewCustomer):Boolean = {
    trySvcTF { () =>
      SvcHelpers.post(
        serverCfg.addNewCustomerURL,
        Global.getCurrToken,
        AddNewCustomer.toJson(nc)
      )
      true
    }
  }


  def findCustomerById(customerId:String):MCustomer = {
    trySvc { () =>
      val j = SvcHelpers.post(
        serverCfg.customerByIdURL,
        Global.getCurrToken,
        customerId
      )
      if (j.isEmpty) null
      else MCustomer.fromJson(j)
    }
  }
  def updateAllRefedCustomers(profId:String, userToken:String):Array[MCustomer] = {
    val j = SvcHelpers.post(
      serverCfg.customersRefedByURL,
      userToken,
      profId
    )

    MCustomer.fromJsons(j)
  }

  def updateMedProfsOf(orgAgentId:String, userToken:String):Array[MMedProf] = {
    val j = SvcHelpers.post(
      serverCfg.medprofsOfURL,
      userToken,
      orgAgentId
    )

    MMedProf.fromJsons(j)
  }

  def updateCustomerPassword(up:UpdatePassword, userToken:String):Unit = {
    val j = UpdatePassword.toJson(up)
    val resp = SvcHelpers.postCheckStatus(
      serverCfg.updateCustomerPasswordURL,
      userToken,
      j
    )
    if (!resp.success) Global.loggingTodo(resp.errMsgJ)
    else {
      Global.loggingTodo(
        s"pass updated for user ${Global.getCurrUid}"
      )
    }
  }

  def updateProfOrgAgentsOf(userToken:String):Array[MProfOrgAgent] = {
    SvcHelpers.getDecArray(
      serverCfg.profOrgAgentsOfURL,
      userToken,
      MProfOrgAgent.fromJsons
    )
  }

  def updateOrgAgentOrderStats(orgAgentId:String, userToken:String):Array[MOrgAgentOrderStat] = {
    val j = SvcHelpers.post(
      serverCfg.orderStatsOfAgentURL,
      userToken,
      orgAgentId
    )

    MOrgAgentOrderStat.fromJsons(j)
  }

  def updateOrgOrderStats(userToken:String):Array[MOrgOrderStat] = {
    SvcHelpers.getDecArray(
      serverCfg.orderStatsOfOrgURL,
      userToken,
      MOrgOrderStat.fromJsons
    )
  }


  def updateAllOrdersOfRefedCustomers(profId:String, userToken:String):Array[MOrder] = {
    val j = SvcHelpers.post(
      serverCfg.refedCustomerOrdersURL,
      userToken,
      profId
    )

    MOrder.fromJsons(j)
  }

//  def updateAllOrders(token:String): Array[Order] = {
//    val cfg = serverCfg
//
//    val j = SvcHelpers.get(cfg.currOrdersURL, token)
//    val morders = MOrder.fromJsons(j)
//    val orders = Helpers.convOrders(morders, Global.getProductMap)
//    orders
//  }

  def updateAgentRewardPlans(userToken:String):Array[MRewardPlanMap] =
    SvcHelpers.getRewardPlan4Org(serverCfg.agentRewardPlansURL, userToken)

  def updateRewardPlan4User(uid:String, userToken:String):TRewardPlan =
    SvcHelpers.getRewardPlan4UserJ(serverCfg.rewardPlanURL, uid, userToken)

  def updateRewardPlans(userToken:String):Array[MRewardPlan] =
    SvcHelpers.getDecArray(serverCfg.orgRewardPlansURL, userToken, MRewardPlan.fromJsons)

  def updatePricePlansOfProf(profId:String, token:String):Array[MPricePlan] = {
    val mps = SvcHelpers.postDecArray(
      serverCfg.pricePlanAccessibleByURL,
      token,
      profId,
      MPricePlan.fromJsons
    )

    mps
  }

  import collection.JavaConverters._
  def updatePricePlans4Customers(profId:String, token:String):java.util.Map[String, MPricePlan] = {
    val cps = SvcHelpers.postDecArray(
      serverCfg.customerPricePlansURL,
      token,
      profId,
      CustomerPricePlan.fromJsons
    )

    cps.map(c => c.customerId -> c.pricePlan).toMap.asJava
  }

  def updateRewardPlansCreatedBy(uid:String, token:String):Array[MRewardPlan] = {
    val mps = SvcHelpers.postDecArray(
      serverCfg.rewardPlanAccessibleByURL,
      token,
      uid,
      MRewardPlan.fromJsons
    )

    mps
  }
}
