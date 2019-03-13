package org.xg.svc

import org.xg.json.CommonUtils
import org.xg.pay.pricePlan.PricePlanSettings.VTag
import org.xg.pay.pricePlan.PricePlanSettings.VTag._
import org.xg.pay.pricePlan.v1.PrPlChained
import org.xg.pay.pricePlan.{PricePlanSettings, TPricePlan}


object UserPricePlanUtils {

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
}
