package org.xg.svc

import org.xg.json.CommonUtils

case class ImageInfo(
  prodId:Int,
  relUrl:String
) {
  def localPath(rootPath:String):String = s"$rootPath/$prodId/$relUrl"
}

object ImageInfo {
  def toJson(imgInfo:ImageInfo):String = {
    CommonUtils._toJson(imgInfo)
  }

  def fromJson(json:String):ImageInfo = {
    CommonUtils._fromJson(json)
  }
}
