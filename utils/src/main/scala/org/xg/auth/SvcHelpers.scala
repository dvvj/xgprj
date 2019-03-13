package org.xg.auth

import java.time.ZonedDateTime

import org.xg.svc.UserPass
import scalaj.http.{Http, HttpOptions}
import java.util.function.{Function => JFunc}

import org.xg.pay.pricePlan.TPricePlan
import org.xg.pay.pricePlan.v1.PrPlFixedRate

object SvcHelpers {

  import AuthResp._

  def dbgGetPricePlanViaSvc = PrPlFixedRate(0.7)

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

  def getDecArrayJ[T](url:String, token:String, decoder:JFunc[String, Array[T]]):Array[T] = {
    val res = get(url, token)
    decoder(res)
  }


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

}
