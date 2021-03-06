package org.xg.hbn

import org.hibernate.{Session, SessionFactory}
import org.xg.dbModels._
import org.xg.gnl.DataUtils
import org.xg.hbn.utils.HbnUtils

object Helpers {

  def runInTransaction(sessFactory:SessionFactory, action: Session => Unit):Unit = {
    val sess = sessFactory.getCurrentSession
    val tr = sess.beginTransaction()
    try {
      action(sess)
      tr.commit()
    }
    catch {
      case t:Throwable => {
        tr.rollback()
        //        t.printStackTrace()
        throw t
      }
    }
  }

  def runInTransaction[T](sessFactory:SessionFactory, f: Session => T):T = {
    val sess = sessFactory.getCurrentSession
    val tr = sess.beginTransaction()
    try {
      val res = f(sess)
      tr.commit()
      res
    }
    catch {
      case t:Throwable => {
        tr.rollback()
        //        t.printStackTrace()
        throw t
      }
    }
  }

  def testRunInTransaction[T](f: Session => T):T = {
    runInTransaction(HbnUtils.testSessFactory, f)
  }

  import org.xg.hbn.ent._

  def convertProduct(prod:Product, asset:Asset):MProduct = {
    val assetItems = AssetCfg.assetsFromJsons(asset.getAssetJson)
    MProduct(
      prod.getId,
      prod.getName,
      prod.getPrice0,
      prod.getDetailedInfo,
      prod.getKeywords,
      prod.getCategories,
      AssetCfg(prod.getId, assetItems)
    )
  }

  def convertCustomer(c:Customer):MCustomer = {
    MCustomer(c.getUid, c.getName, c.getIdCardNo, c.getMobile, c.getPostalAddr, c.getBday, c.getRefUid)
  }

  def convertPricePlan(pp:PricePlan):MPricePlan = {
    MPricePlan(pp.getId, pp.getInfo, pp.getDefi, pp.getVtag, pp.getCreator)
  }

  def convertRewardPlan(pp:RewardPlan):MRewardPlan = {
    MRewardPlan(pp.getId, pp.getInfo, pp.getDefi, pp.getVtag, pp.getCreator)
  }

  def convertOrgOrderStat(os:OrgOrderStat):MOrgOrderStat = {
    MOrgOrderStat(os.getOrgId, os.getOrgAgentId, os.getProfId, os.getOrderId,
      os.getProductId, os.getQty, os.getActualCost,
      DataUtils.zonedDateTime2Str(os.getCreationTime),
      os.getStatus
    )
  }

  def convertSvcAudit(sa:SvcAudit):MSvcAudit = {
    MSvcAudit(
      sa.getId, sa.getOps,
      DataUtils.zonedDateTime2Str(sa.getTs),
      sa.getStatus, sa.getDuration,
      if (sa.getUid != null) Option(sa.getUid) else None,
      if (sa.getExtra != null) Option(sa.getExtra) else None
    )
  }

  def convertCustomerProfile(cp:CustomerProfile):MCustomerProfile = {
    MCustomerProfile(
      cp.getId,
      cp.getProfId,
      cp.getCustomerId,
      cp.getDetailedInfo,
      cp.getVersion
    )
  }

  import DataUtils._
  def convertPricePlanMap(ppm:PricePlanMap):MPricePlanMap = {
    MPricePlanMap.createJ(ppm.getUid, ppm.getPlanIds,
      zonedDateTime2Str(ppm.getStartTime),
      zonedDateTime2Str(ppm.getExpireTime)
    )
  }
  def revConvertPricePlanMap(ppm:MPricePlanMap):PricePlanMap = {
    new PricePlanMap(
      ppm.uid, ppm._planIds, ppm.getStartTime, ppm.getExpireTime.orNull
    )
  }
  def revConvertRewardPlanMap(rpm:MRewardPlanMap):RewardPlanMap = {
    new RewardPlanMap(
      rpm.uid, rpm._planIds, rpm.getStartTime, rpm.getExpireTime.orNull
    )
  }
  def convertRewardPlanMap(ppm:RewardPlanMap):MRewardPlanMap = {
    MRewardPlanMap.createJ(ppm.getUid, ppm.getPlanIds,
      zonedDateTime2Str(ppm.getStartTime),
      zonedDateTime2Str(ppm.getExpireTime)
    )
  }
  def convertMedProf(mp:MedProf):MMedProf = {
    MMedProf(mp.getProfId, mp.getName,mp.getIdCardNo, mp.getMobile, mp.getOrgAgentId)
  }

  def convertOrgOrderStat(o:OrgAgentOrderStat):MOrgAgentOrderStat = {
    MOrgAgentOrderStat(
      o.getOrgAgentId, o.getProfId, o.getOrderId, o.getProductId,
      o.getQty, o.getActualCost, zonedDateTime2Str(o.getCreationTime), o.getStatus
    )
  }

  def convertProfOrg(po:ProfOrgAgent):MProfOrgAgent = {
    MProfOrgAgent(
      po.getOrgAgentId,
      po.getOrgId,
      po.getName,
      po.getInfo,
      po.getPhone,
      zonedDateTime2Str(po.getJoinDate)
    )
  }

  def convertOrder(o:Order):MOrder = {
    MOrder.createJ(
      o.getId, o.getCustomerId, o.getProductId, o.getQty, o.getActualCost,
      zonedDateTime2Str(o.getCreationTime),
      zonedDateTime2Str(o.getPayTime),
      zonedDateTime2Str(o.getProcTime1),
      zonedDateTime2Str(o.getProcTime2),
      zonedDateTime2Str(o.getProcTime3)
    )
  }

  def convertOrderHistory(oh:OrderHistory):MOrderHistory = {
    MOrderHistory(
      oh.getOrderId, zonedDateTime2Str(oh.getUpdateTime), oh.getOldQty
    )
  }

  def loggingTodo(msg:String):Unit = {
    println(msg)
  }
}
