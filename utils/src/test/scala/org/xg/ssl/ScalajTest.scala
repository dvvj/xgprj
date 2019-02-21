package org.xg.ssl

import org.xg.gnl.GlobalCfg
import scalaj.http.{Http, HttpOptions}

object ScalajTest extends App {

  val url = GlobalCfg.localTestCfg.allProductsURL

  val res = Http(url).option(HttpOptions.allowUnsafeSSL)
    .asString

  println(res.body)
}
