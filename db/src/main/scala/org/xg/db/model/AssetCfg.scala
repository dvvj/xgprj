package org.xg.db.model

import AssetCfg._
import org.json4s.DefaultFormats
import org.xg.json.CommonUtils
case class AssetItem(
  url:String,
  typ:String,
  cat:String
)

case class AssetCfg(
  prodId:Int,
  assets:Array[AssetItem]
)

object AssetCfg {
  private[model] val TYP_IMG = "img"
  private[model] val TYP_DOC = "doc"

  def imageAsset(url:String, cat:String):AssetItem = {
    AssetItem(url, TYP_IMG, cat)
  }
  def docAsset(url:String, cat:String):AssetItem = {
    AssetItem(url, TYP_DOC, cat)
  }

  def asset(prodId:Int, imgs:Array[Array[String]], docs:Array[Array[String]]):AssetCfg = {
    val imgAssets = imgs.map { urlCat =>
      val (url, cat) = urlCat(0) -> urlCat(1)
      imageAsset(url, cat)
    }
    val docAssets = docs.map { urlCat =>
      val (url, cat) = urlCat(0) -> urlCat(1)
      docAsset(url, cat)
    }
    AssetCfg(prodId, imgAssets ++ docAssets)
  }

  def assetsToJsons(assets:Array[AssetItem]):String = {
    CommonUtils._toJsons(assets)
  }

  def assetsFromJsons(j:String):Array[AssetItem] = {
    CommonUtils._fromJsons(j)
  }
}
