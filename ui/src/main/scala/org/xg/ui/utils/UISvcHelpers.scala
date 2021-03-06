package org.xg.ui.utils

import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.layout.StackPane
import org.xg.auth.SvcHelpers
import org.xg.busiLogic.PricePlanLogics
import org.xg.dbModels.MCustomerProfile.ProfileDetailV1_00
import org.xg.dbModels._
import org.xg.gnl.{DataUtils, GlobalCfg}
import org.xg.json.CommonUtils
import org.xg.pay.pricePlan.TPricePlan
import org.xg.pay.rewardPlan.TRewardPlan
import org.xg.svc.{AddNewCustomer, AddNewMedProf, CustomerPricePlan, UpdatePassword}
import org.xg.ui.error.SvcException
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

  def trySvcT[T](
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

  def trySvc(
    action:() => OpResp,
    dlgContainer: StackPane
  ):Unit = {
    var opResp:OpResp = null
    try {
      opResp = action()
    }
    catch {
      case t:Throwable => {
        Global.loggingTodo(s"Error adding new med prof: ${t.getMessage}")
        throw t
      }
    }

    if (!opResp.success) {
      UIHelpers.errorDialog(opResp.errMsgJ, dlgContainer)
    }
  }

  def handleOpResp(resp:OpResp):Unit = {
    if (!resp.success) {
      throw SvcException.create(resp.errMsgJ)
    }
  }

  def addNewMedProf(mp:AddNewMedProf, dlgContainer: StackPane):Unit = {
    trySvc( () =>
      SvcHelpers.postCheckStatus(
        serverCfg.addNewMedProfURL,
        Global.getCurrToken,
        AddNewMedProf.toJson(mp)
      ),
      dlgContainer
    )
  }

  def addNewCustomer(nc:AddNewCustomer):OpResp = {
    trySvcT { () =>
      val res = SvcHelpers.postCheckStatus(
        serverCfg.addNewCustomerURL,
        Global.getCurrToken,
        AddNewCustomer.toJson(nc)
      )
      res
    }
  }

  private val NewCustomerProfileID_NA = -1
  def createProfileV1_00(
                          profId:String,
                          customerId:String,
                          productIds:Array[Int],
                          pricePlanId:String
                        ):Long = {
    val creationTime = DataUtils.utcTimeNowStr
    val detail = ProfileDetailV1_00(productIds, pricePlanId, creationTime)
    val detailStr = CommonUtils._toJson(detail)
    val customerProfile = MCustomerProfile(NewCustomerProfileID_NA, profId, customerId, detailStr, MCustomerProfile.DetailVersion1_00)
    val postJson = CommonUtils._toJson(customerProfile)
    val newProfileId = SvcHelpers.post(serverCfg.newProfileExistingCustomerURL, Global.getCurrToken, postJson)
    newProfileId.toLong
  }

  def findCustomerById(customerId:String):MCustomer = {
    trySvcT { () =>
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
    SvcHelpers.getDecArray(
      serverCfg.customersRefedByURL,
      userToken,
      MCustomer.fromJsons
    )
  }

  def updateMedProfsOf(orgAgentId:String, userToken:String):Array[MMedProf] = {
    SvcHelpers.getDecArray(
      serverCfg.medprofsOfURL,
      userToken,
      MMedProf.fromJsons
    )
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
    SvcHelpers.getDecArray(
      serverCfg.orderStatsOfAgentURL,
      userToken,
      MOrgAgentOrderStat.fromJsons
    )
  }

  def updateOrgOrderStats(userToken:String):Array[MOrgOrderStat] = {
    SvcHelpers.getDecArray(
      serverCfg.orderStatsOfOrgURL,
      userToken,
      MOrgOrderStat.fromJsons
    )
  }


  def updateAllOrdersOfRefedCustomers(profId:String, userToken:String):Array[MOrder] = {
    SvcHelpers.getDecArray(
      serverCfg.refedCustomerOrdersURL,
      userToken,
      MOrder.fromJsons
    )
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
    SvcHelpers.getDecArray(
      serverCfg.pricePlanAccessibleByURL,
      token,
      MPricePlan.fromJsons
    )
  }

  import collection.JavaConverters._
  def updatePricePlans4Customers(profId:String, token:String):java.util.Map[String, MPricePlan] = {
    val cps = SvcHelpers.getDecArray(
      serverCfg.customerPricePlansURL,
      token,
      CustomerPricePlan.fromJsons
    )

    cps.map(c => c.customerId -> c.pricePlan).toMap.asJava
  }

  def updateRewardPlansCreatedBy(uid:String, token:String):Array[MRewardPlan] = {
    SvcHelpers.getDecArray(
      serverCfg.rewardPlanAccessibleByURL,
      token,
      MRewardPlan.fromJsons
    )
  }
}
