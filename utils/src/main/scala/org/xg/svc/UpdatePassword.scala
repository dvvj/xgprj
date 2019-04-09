package org.xg.svc

import org.xg.json.CommonUtils

case class UpdatePassword(
  oldpassHash:String,
  newpassHash:String
) {

}

object UpdatePassword {
  def fromJson(j:String):UpdatePassword = CommonUtils._fromJson[UpdatePassword](j)
  def toJson(up:UpdatePassword):String = CommonUtils._toJson(up)
}