package org.xg.svc

import org.xg.dbModels.{MMedProf, MRewardPlan, MRewardPlanMap}
import org.xg.json.CommonUtils

case class AddNewMedProf(
  medProf:MMedProf,
  pass:String,
  rewardPlan:MRewardPlanMap
)

object AddNewMedProf {
  def toJson(o:AddNewMedProf):String =
    CommonUtils._toJson[AddNewMedProf](o)


  def fromJson(j:String):AddNewMedProf =
    CommonUtils._fromJson[AddNewMedProf](j)
}