package org.xg.auth

import org.xg.svc.UserPass
import scalaj.http.{Http, HttpOptions}

object SvcHelpers {

  def authReq(url:String, uid:String, pass:String):String = {
    val up = UserPass.fromUserPass(uid, pass)
    val j = UserPass.toJson(up)

    val res = Http(url)
      .postData(j)
      .method("POST")
      .header("content-type", "text/plain")
      .option(HttpOptions.allowUnsafeSSL)
      .asString

    res.body
  }

}
