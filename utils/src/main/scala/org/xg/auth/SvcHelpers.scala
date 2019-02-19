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

}
