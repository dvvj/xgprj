package org.xg.dbModels

import org.xg.json.CommonUtils

case class MSvcAudit(
  id:Long,
  ops:String,
  _ts:String,
  status:Int,
  duration:Int,
  uid:Option[String],
  extra:Option[String]
) {

}

object MSvcAudit {

  val StatusOK:Int = 0
  val StatusException:Int = 1

  def toJsons(agents:Array[MSvcAudit]):String = CommonUtils._toJsons(agents)
  def fromJsons(j:String):Array[MSvcAudit] = CommonUtils._fromJsons(j)

}
