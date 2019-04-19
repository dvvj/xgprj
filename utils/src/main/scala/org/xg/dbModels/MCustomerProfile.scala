package org.xg.dbModels

import org.xg.json.CommonUtils

case class MCustomerProfile(
  id:Long,
  profId:String,
  customerId:String,
  detailedInfo:String,
  version:String
) {
  import MCustomerProfile._

  private val _detail:TProfileDetail = decodeProfile(version, detailedInfo)
  def getDetail:TProfileDetail = _detail

}

object MCustomerProfile {
  def fromJsons(j:String):Array[MCustomerProfile] = CommonUtils._fromJsons(j)
  def toJsons(profiles:Array[MCustomerProfile]):String = CommonUtils._toJsons(profiles)

  trait TProfileDetail {
    def productIds:Array[Int]
    def pricePlanId:String
    def creationTime:String
  }

  case class ProfileDetailV1_00(
    productIds:Array[Int],
    pricePlanId:String,
    creationTime:String
  ) extends TProfileDetail

  type ProfileDecoder = String => TProfileDetail
  private val decoderMap:Map[String, ProfileDecoder] = Map(
    "1.00" -> (j => CommonUtils._fromJson[ProfileDetailV1_00](j))
  )

  def decodeProfile(ver:String, profileDetail:String):TProfileDetail = {
    decoderMap(ver)(profileDetail)
  }
}