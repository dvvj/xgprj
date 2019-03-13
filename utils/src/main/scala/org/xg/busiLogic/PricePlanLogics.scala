package org.xg.busiLogic

import org.xg.dbModels.{MCustomer, MPricePlan, MPricePlanMap}
import org.xg.gnl.DataUtils.{utcTimeNow, zonedDateTime2Ms}
import org.xg.pay.pricePlan.TPricePlan
import org.xg.pay.pricePlan.v1.PrPlChained

object PricePlanLogics {
  def activePricePlans(allPlans:Array[MPricePlanMap]):Map[String, MPricePlanMap] = {
    allPlans.groupBy(_.uid).flatMap { p =>
      val pps = p._2.filter { ppm =>
        if (ppm.getExpireTime.nonEmpty) {
          val expTime = ppm.getExpireTime.get
          expTime.compareTo(utcTimeNow) > 0
        }
        else
          true
      }
      if (pps.nonEmpty)
        Option(p._1 -> pps.maxBy(ppm => zonedDateTime2Ms(ppm.getStartTime)))
      else
        None
    }
  }

  def pricePlanFor(customer:MCustomer, planMap:Map[String, MPricePlanMap], plans:Map[String, MPricePlan]):Option[TPricePlan] = {
    val planIds =
      if (planMap.contains(customer.uid)) planMap(customer.uid).getPlanIds
      else if (planMap.contains(customer.refUid)) planMap(customer.refUid).getPlanIds
      else Array[String]()

    if (planIds.nonEmpty) {
      val res =
        if (planIds.length == 1)
          plans(planIds(0)).getPlan
        else {
          val chain = planIds.map(plans).map(_.getPlan).toList
          PrPlChained.create(chain)
        }
      Option(res)
    }
    else None

  }

  import collection.JavaConverters._
  def pricePlanForJ(customer:MCustomer, planMap:java.util.Map[String, MPricePlanMap], plans:java.util.Map[String, MPricePlan]):TPricePlan = {
    val res = pricePlanFor(customer, planMap.asScala.toMap, plans.asScala.toMap)
    res.orNull
  }
}
