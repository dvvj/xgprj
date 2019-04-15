package org.xg.dbModels

case class MProduct(
  id:Int,
  name:String,
  price0:Double,
  detailedInfo:String,
  keywords:String,
  categories:String,
  assets:AssetCfg
) {
  def keywordsArr:Array[String] = keywords.split(",").map(_.trim).filter(!_.isEmpty)
  private val _cats:Array[Int] = categories.split(",")
    .map(_.trim).filter(!_.isEmpty)
    .map(_.toInt)
  def getCategories:Array[Int] = _cats
  def prodDetail:MProdDetail = MProdDetail.fromJson(detailedInfo)
}

object MProduct {
  import org.xg.json.CommonUtils._
  def toJsons(products:Array[MProduct]):String = _toJsons(products)
  def fromJsons(j:String):Array[MProduct] = _fromJsons(j)
}
