package org.xg.ui.utils

import org.xg.auth.SvcHelpers
import org.xg.dbModels.{MCustomer, MOrder}
import org.xg.gnl.GlobalCfg
import org.xg.pay.rewardPlan.TRewardPlan

object UISvcHelpers {

  val clientCfg = ClientCfg(
    UserCfg("customer1", 0, ""),
    GlobalCfg.localTestCfg // todo
  )

  val serverCfg:GlobalCfg = clientCfg.serverCfg

  def updateAllRefedCustomers(profId:String, userToken:String):Array[MCustomer] = {
    val j = SvcHelpers.post(
      serverCfg.customersRefedByURL,
      userToken,
      profId
    )

    MCustomer.fromJsons(j)
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
