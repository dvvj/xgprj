package org.xg.auth

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object AuthHelpers {

  def sha512(msg:String):Array[Byte] = {
    val md = MessageDigest.getInstance("SHA-512")
    val res = md.digest(msg.getBytes(StandardCharsets.UTF_8))
    res
  }

  def hash2Str(hash:Array[Byte]):String = {
    hash.map { b =>
      val bv = b & 0xff
      f"$bv%02x"
    }.mkString
  }

}
