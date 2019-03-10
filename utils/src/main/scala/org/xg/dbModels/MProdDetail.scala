package org.xg.dbModels

case class MProdDetail(
  srcCountry:String,
  desc:String
)

object MProdDetail {
  import org.xg.json.CommonUtils._
  def toJson(detail:MProdDetail):String = _toJson(detail)
  def fromJson(j:String):MProdDetail = _fromJson[MProdDetail](j)
}
