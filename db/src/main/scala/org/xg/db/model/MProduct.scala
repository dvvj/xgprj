package org.xg.db.model

case class MProduct(
  id:Int,
  name:String,
  price0:Double,
  detailedInfo:String,
  keywords:String,
  assets:AssetCfg
) {
  def keywordsArr:Array[String] = keywords.split(",").map(_.trim).filter(!_.isEmpty)
}

object MProduct {
  import org.xg.json.CommonUtils._
  def toJsons(products:Array[MProduct]):String = _toJsons(products)
  def fromJsons(j:String):Array[MProduct] = _fromJsons(j)

}
