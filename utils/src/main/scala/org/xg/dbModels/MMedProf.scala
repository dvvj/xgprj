package org.xg.dbModels

import org.xg.json.CommonUtils

case class MMedProf(
  profId:String,
  name:String,
  idCardNo:String,
  mobile:String,
  orgId:String
)

object MMedProf {
  def toJsons(medprofs:Array[MMedProf]):String =
    CommonUtils._toJsons(medprofs)
  def fromJsons(j:String):Array[MMedProf] =
    CommonUtils._fromJsons(j)
}