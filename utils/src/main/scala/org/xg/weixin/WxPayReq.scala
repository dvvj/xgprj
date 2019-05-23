package org.xg.weixin

import org.xg.json.CommonUtils

case class WxPayReq(
                   userId:String,
                   actId:String,
                   prodId:String,
                   openId:String,
                   amount:Int,
                   info:String
                   ) {

}

object WxPayReq {
  def fromJson(j:String):WxPayReq = CommonUtils._fromJson[WxPayReq](j)
}