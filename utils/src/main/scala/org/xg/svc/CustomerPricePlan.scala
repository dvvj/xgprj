package org.xg.svc

import org.xg.dbModels.MPricePlan
import org.xg.json.CommonUtils

case class CustomerPricePlan(
                            customerId:String,
                            pricePlan:MPricePlan
                            ) {

}

object CustomerPricePlan {
  def toJsons(d:Array[CustomerPricePlan]):String = CommonUtils._toJsons(d)

  def fromJsons(j:String):Array[CustomerPricePlan] = CommonUtils._fromJsons(j)
}