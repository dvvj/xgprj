package org.xg.ui.utils

import org.xg.auth.SvcHelpers
import org.xg.dbModels.{MCustomer, MMedProf, MOrder, MOrgOrderStat}
import org.xg.gnl.GlobalCfg
import org.xg.pay.rewardPlan.TRewardPlan
import org.xg.svc.{AddNewCustomer, AddNewMedProf}

object UISvcHelpers {

  val clientCfg = ClientCfg(
    UserCfg("customer1", 0, ""),
    GlobalCfg.localTestCfg // todo
  )

  val serverCfg:GlobalCfg = clientCfg.serverCfg

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

  def updateOrgOrderStats(orgId:String, userToken:String):Array[MOrgOrderStat] = {
    val j = SvcHelpers.post(
      serverCfg.orderStatsOfURL,
      userToken,
      orgId
    )

    MOrgOrderStat.fromJsons(j)
  }


  def updateAllOrdersOfRefedCustomers(profId:String, userToken:String):Array[MOrder] = {
    val j = SvcHelpers.post(
      serverCfg.refedCustomerOrdersURL,
      userToken,
      profId
    )

    MOrder.fromJsons(j)
  }

  def updateRewardPlans(profId:String, userToken:String):TRewardPlan =
    SvcHelpers.getRewardPlan4UserJ(serverCfg.rewardPlanURL, profId, userToken)
}
