package org.xg.ssl

import scalaj.http.{Http, HttpOptions}

object ScalajTest extends App {

  val url = "https://localhost:8443/webapi/db/allProducts"

  val res = Http(url).option(HttpOptions.allowUnsafeSSL)
    .asString

  println(res.body)
}
