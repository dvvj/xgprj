package org.xg.ui.utils

import javafx.collections.{FXCollections, ObservableList}
import org.xg.auth.SvcHelpers
import org.xg.dbModels.{MCustomer, MMedProf, MOrder, MOrgAgentOrderStat}
import org.xg.gnl.GlobalCfg
import org.xg.pay.rewardPlan.TRewardPlan
import org.xg.svc.{AddNewCustomer, AddNewMedProf}
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


  def updateAllRefedCustomers(profId:String, userToken:String):Array[MCustomer] = {
    val j = SvcHelpers.post(
      serverCfg.customersRefedByURL,
      userToken,
      profId
    )

    MCustomer.fromJsons(j)
  }

  def updateMedProfsOf(orgId:String, userToken:String):Array[MMedProf] = {
    val j = SvcHelpers.post(
      serverCfg.medprofsOfURL,
      userToken,
      orgId
    )

    MMedProf.fromJsons(j)
  }

  def updateOrgOrderStats(orgId:String, userToken:String):Array[MOrgAgentOrderStat] = {
    val j = SvcHelpers.post(
      serverCfg.orderStatsOfURL,
      userToken,
      orgId
    )

    MOrgAgentOrderStat.fromJsons(j)
  }


  def updateAllOrdersOfRefedCustomers(profId:String, userToken:String):Array[MOrder] = {
    val j = SvcHelpers.post(
      serverCfg.refedCustomerOrdersURL,
      userToken,
      profId
    )

    MOrder.fromJsons(j)
  }

  def updateAllOrders(token:String): Array[Order] = {
    val cfg = UISvcHelpers.serverCfg

    val j = SvcHelpers.get(cfg.currOrdersURL, token)
    val morders = MOrder.fromJsons(j)
    val orders = Helpers.convOrders(morders, Global.getProductMap)
    orders
  }

  def updateRewardPlans(uid:String, userToken:String):TRewardPlan =
    SvcHelpers.getRewardPlan4UserJ(serverCfg.rewardPlanURL, uid, userToken)
}
