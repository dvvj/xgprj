package org.xg.auth

import java.time.ZonedDateTime

import org.xg.svc.UserPass
import scalaj.http.{Http, HttpOptions}

object SvcHelpers {

  import AuthResp._

  def authReq(url:String, uid:String, pass:String):AuthResp = {
    val up = UserPass.fromUserPass(uid, pass)
    val j = UserPass.toJson(up)

    val res = Http(url)
      .postData(j)
      .method("POST")
      .header("content-type", "text/plain")
      .option(HttpOptions.allowUnsafeSSL)
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

  def getDecArray[T](url:String, token:String, decoder:String => Array[T]):Array[T] = {
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
