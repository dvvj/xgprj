package org.xg.svc

import org.xg.gnl.GlobalCfg
import org.xg.json.CommonUtils

case class ImageInfo(
  prodId:Int,
  relUrl:String
) {
  def localPath(cfg:GlobalCfg):String = s"${cfg.assetLocalPath}/$prodId/$relUrl"
  def url(cfg:GlobalCfg):String = s"${cfg.imgAssetURL}/$prodId/$relUrl"
  def getUrl(cfg:GlobalCfg):String = s"${cfg.imgAssetURL}?prodId=$prodId&imageName=$relUrl"
}

object ImageInfo {
  def toJson(imgInfo:ImageInfo):String = {
    CommonUtils._toJson(imgInfo)
  }

  def fromJson(json:String):ImageInfo = {
    CommonUtils._fromJson[ImageInfo](json)
  }
}
