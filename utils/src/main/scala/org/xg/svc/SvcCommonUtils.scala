package org.xg.svc

import org.xg.json.CommonUtils
import org.xg.pay.pricePlan.v1.PrPlChained
import org.xg.pay.pricePlan.{PricePlanSettings, TPricePlan}
import org.xg.pay.rewardPlan.v1.RwPlChained
import org.xg.pay.rewardPlan.{RewardPlanSettings, TRewardPlan}


object SvcCommonUtils {

  object PricePlanJson {
    import org.xg.pay.pricePlan.PricePlanSettings.VTag
    import org.xg.pay.pricePlan.PricePlanSettings.VTag._
    case class PricePlan4Svc(
      _vtag:String,
      defi:String
    ) {
      private val vtag:VTag = VTag.withName(_vtag)
      def getVTag:VTag = vtag
    }

    def convert2Json(plans:Array[(VTag, String)]):String = {
      val t = plans.map(p => PricePlan4Svc(p._1.toString, p._2))
      CommonUtils._toJsons(t)
    }
  }

  object RewardPlanJson {
    import org.xg.pay.rewardPlan.RewardPlanSettings.VTag
    import org.xg.pay.rewardPlan.RewardPlanSettings.VTag._
    case class RewardPlan4Svc(
      _vtag:String,
      defi:String
    ) {
      private val vtag:VTag = VTag.withName(_vtag)
      def getVTag:VTag = vtag
    }

    def convert2Json(plans:Array[(VTag, String)]):String = {
      val t = plans.map(p => RewardPlan4Svc(p._1.toString, p._2))
      CommonUtils._toJsons(t)
    }
  }

  import PricePlanJson._

  def decodePricePlanJson(j:String):Option[TPricePlan] = {
    val plans4Svc = CommonUtils._fromJsons[PricePlan4Svc](j)
    val decodedPlans = plans4Svc.map(p => PricePlanSettings.decodePlan(p.getVTag, p.defi))
    if (decodedPlans.length == 1) {
      Option(decodedPlans(0))
    }
    else if (decodedPlans.length > 1) {
      Option(
        PrPlChained.create(decodedPlans.toList)
      )
    }
    else None // no plan found
  }

  import RewardPlanJson._

  def decodeRewardPlanJson(j:String):Option[TRewardPlan] = {
    val plans4Svc = CommonUtils._fromJsons[RewardPlan4Svc](j)
    val decodedPlans = plans4Svc.map(p => RewardPlanSettings.decodePlan(p.getVTag, p.defi))
    if (decodedPlans.length == 1) {
      Option(decodedPlans(0))
    }
    else if (decodedPlans.length > 1) {
      Option(
        RwPlChained.create(decodedPlans.toList)
      )
    }
    else None // no plan found
  }

}
