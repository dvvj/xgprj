package org.xg.auth

object SvcHelpersTest extends App {

  val authUrl = "https://localhost:443/webapi/auth/userPass"
  val resp = SvcHelpers.authReq(
    authUrl,
    "customer3",
    "abcdef"
  )

  println(resp)

}
