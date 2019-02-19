package org.xg.auth

object Sha256Test extends App {
  val testStrs = Array(
    "test1",
    "abcdef",
    "abc!df9B"
  )

  testStrs.foreach { msg =>
    val res = AuthHelpers.sha512(msg)
    val passHash = AuthHelpers.hash2Str(res)
    println(s"${res.length}: $passHash")
  }

  val token = AuthHelpers.generateToken(
    Array("usr1", "ut1", "hash1")
  )
  println(token)
}
