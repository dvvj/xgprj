package org.xg.svc

import org.xg.dbModels.{MCustomer, MPricePlanMap}
import org.xg.json.CommonUtils

case class AddNewCustomer(
  customer:MCustomer,
  pass:String,
  ppm:MPricePlanMap
)

object AddNewCustomer {
  def toJson(o:AddNewCustomer):String =
    CommonUtils._toJson[AddNewCustomer](o)


  def fromJson(j:String):AddNewCustomer =
    CommonUtils._fromJson[AddNewCustomer](j)
}
