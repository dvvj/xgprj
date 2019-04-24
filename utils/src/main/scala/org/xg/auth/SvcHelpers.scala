package org.xg.auth

import java.time.ZonedDateTime

import org.xg.svc.{SvcCommonUtils, UserPass}
import scalaj.http.{Http, HttpOptions}
import java.util.function.{Function => JFunc}

import org.xg.dbModels.{MPricePlan, MRewardPlan, MRewardPlanMap, OpResp}
import org.xg.pay.pricePlan.TPricePlan
import org.xg.pay.pricePlan.v1.PrPlFixedRate
import org.xg.pay.rewardPlan.TRewardPlan

object SvcHelpers {

  import AuthResp._

  //def dbgGetPricePlanViaSvc = PrPlFixedRate(0.7)
  def getPricePlan4User(url:String, token:String):Option[TPricePlan] = {
    val plansJson = get(url, token)
    SvcCommonUtils.decodePricePlanJson(plansJson)
  }
//  def getPricePlan4UserJ(url:String, token:String):TPricePlan = {
//    getPricePlan4User(url, token).orNull
//  }
  def getRewardPlan4User(url:String, uid:String, token:String):Option[TRewardPlan] = {
    val plansJson = post(url, token, uid)
    SvcCommonUtils.decodeRewardPlanJson(plansJson)
  }
  def getRewardPlan4UserJ(url:String, uid:String, token:String):TRewardPlan = {
    getRewardPlan4User(url, uid, token).orNull
  }
  def getRewardPlan4Org(url:String, token:String):Array[MRewardPlanMap] = {
    getDecArray(url, token, MRewardPlanMap.fromJsons)
  }

  def authReq(url:String, uid:String, pass:String):AuthResp = {
    val up = UserPass.fromUserPass(uid, pass)
    val j = UserPass.toJson(up)

    val res = Http(url)
      .postData(j)
      .method("POST")
      .header("content-type", "text/plain")
      .option(HttpOptions.allowUnsafeSSL)
//      .timeout(10000, 10000)
      .asString

    if (res.code == 200) {
      fromJson(res.body)
    }
    else {
      val code = res.code
      authFailed(s"Auth failed, code ${code}")
    }
  }

  val AuthHeaderBearer = "Bearer"
  private def encodeAuthHeader(token:String):String = s"$AuthHeaderBearer $token"
  def decodeAuthHeader(authHeader:String):String = {
    authHeader.substring(AuthHeaderBearer.length).trim
  }
  def get(url:String, token:String = ""):String = {
    val header = encodeAuthHeader(token)
    val res = Http(url)
      .option(HttpOptions.allowUnsafeSSL)
      .header("Authorization", header)
      .asString
    res.body
  }

  def get4Bin(url:String, token:String = ""):Array[Byte] = {
    val header = encodeAuthHeader(token)
    val res = Http(url)
      .option(HttpOptions.allowUnsafeSSL)
      .header("Authorization", header)
      .asBytes
    res.body
  }

  def getDecArray[T](url:String, token:String, decoder:String => Array[T]):Array[T] = {
    val res = get(url, token)
    decoder(res)
  }

//  def getDecArrayJ[T](url:String, token:String, decoder:JFunc[String, Array[T]]):Array[T] = {
//    val res = get(url, token)
//    decoder(res)
//  }


  def post(url:String, token:String, data:String):String = {
    val header = encodeAuthHeader(token)
    val res = Http(url)
      .option(HttpOptions.allowUnsafeSSL)
      .postData(data)
      .method("POST")
      .headers(
        Map(
          "content-type" -> "text/plain",
          "Authorization" -> header
        )
      )
      .asString
    res.body
  }

  def postCheckStatus(url:String, token:String, data:String):OpResp = {
    val header = encodeAuthHeader(token)
    val res = Http(url)
      .option(HttpOptions.allowUnsafeSSL)
      .postData(data)
      .method("POST")
      .headers(
        Map(
          "content-type" -> "text/plain",
          "Authorization" -> header
        )
      )
      .asString
    if (res.isSuccess)
      OpResp.Success
    else {
      OpResp.failed(
        s"Error code [${res.code}], msg: [${res.body}]"
      )
    }
  }

  def postWithContentType(url:String, token:String, contentType:String, data:String):String = {
    val header = encodeAuthHeader(token)
    val res = Http(url)
      .option(HttpOptions.allowUnsafeSSL)
      .postData(data)
      .method("POST")
      .headers(
        Map(
          "content-type" -> contentType,
          "Authorization" -> header
        )
      )
      .asString
    res.body
  }

  def post4Bin(url:String, token:String, data:String):Array[Byte] = {
    val header = encodeAuthHeader(token)
    val res = Http(url)
      .option(HttpOptions.allowUnsafeSSL)
      .postData(data)
      .method("POST")
      .headers(
        Map(
          "content-type" -> "text/plain",
          "Authorization" -> header
        )
      )
      .asBytes
    res.body
  }

  def postDecArray[T](url:String, token:String, data:String, decoder:String => Array[T]):Array[T] = {
    val res = post(url, token, data)
    decoder(res)
  }
//  def postDecArrayJ[T](url:String, token:String, data:String, decoder:JFunc[String, Array[T]]):Array[T] = {
//    val res = post(url, token, data)
//    decoder(res)
//  }

  def reqPut(url:String, token:String, data:String):String = {
    val header = encodeAuthHeader(token)
    val res = Http(url)
      .option(HttpOptions.allowUnsafeSSL)
      .put(data)
      .method("PUT")
      .headers(
        Map(
          "content-type" -> "text/plain",
          "Authorization" -> header
        )
      )
      .asString
    res.body
  }

  def addPricePlan(url:String, token:String, plan:MPricePlan):String = {
    val j = MPricePlan.toJson(plan)
    val res = post(url, token, j)
    res
  }

  def addRewardPlan(url:String, token:String, plan:MRewardPlan):String = {
    val j = MRewardPlan.toJson(plan)
    val res = post(url, token, j)
    res
  }

  def pricePlansBy(url:String, token:String, creatorId:String):Array[MPricePlan] = {
    val res = post(url, token, creatorId)
    MPricePlan.fromJsons(res)
  }

  def rewardPlansBy(url:String, token:String, creatorId:String):Array[MRewardPlan] = {
    val res = post(url, token, creatorId)
    MRewardPlan.fromJsons(res)
  }

}
