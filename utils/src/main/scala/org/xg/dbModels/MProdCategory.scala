package org.xg.dbModels

import org.xg.json.CommonUtils

case class MProdCategory(
  id:Int,
  nameIJ:String,
  detailedInfoIJ:String
) {
  private val _nameMap:CCStrMap= IJStr.mapFromJsons(nameIJ)
  def getNameMap:CCStrMap = _nameMap
  private val _detailMap:CCStrMap = IJStr.mapFromJsons(detailedInfoIJ)
  def getDetailMap:CCStrMap = _detailMap
}

object MProdCategory {
  def fromJsons(j:String):Array[MProdCategory] = CommonUtils._fromJsons(j)
  def toJsons(cats:Array[MProdCategory]):String = CommonUtils._toJsons(cats)
}
