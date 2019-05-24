package org.xg.weixin

import org.xg.json.CommonUtils

case class WxLoginReqMP(
                     userId:String,
                     loginCode:String
                   ) {

}


case class WxPayReq(
  userId:String,
//  actId:String,
  prodId:String,
//  openId:String,
  amount:Int,
  info:String
) {

}

case class WxMPPayReq(
  timeStamp:String,
  nonceStr:String,
  `package_`:String,
  signType:String,
  paySign:String
)

object WxPayReq {
  def fromJson(j:String):WxPayReq = CommonUtils._fromJson[WxPayReq](j)
}

object WxMPPayReq {
  def toJson(r:WxMPPayReq):String = CommonUtils._toJson(r)

}

object WxLoginReqMP {
  def fromJson(j:String):WxLoginReqMP = CommonUtils._fromJson[WxLoginReqMP](j)
}