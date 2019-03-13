package org.xg.svc

import org.xg.json.CommonUtils
import org.xg.pay.pricePlan.PricePlanSettings.VTag.VTag


object UserPricePlanUtils {

  case class PricePlan4Svc(
    vtag:String,
    defi:String
  )

  def convert2Json(plans:Array[(VTag, String)]):String = {
    val t = plans.map(p => PricePlan4Svc(p._1.toString, p._2))
    CommonUtils._toJsons(t)
  }
}
