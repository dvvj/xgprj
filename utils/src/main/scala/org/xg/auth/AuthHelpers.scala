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

  private val MsgConnector = "d=3+5*@UsjTE*z23"
  type TokenEncoder = Array[String] => Array[Byte]

  private val DefTokenEncoder:TokenEncoder = msgs => {
    val msg = msgs.mkString(s"\t$MsgConnector\t")
    sha512(msg)
  }

  def generateToken(
                     msgs:Array[String],
                     enc:TokenEncoder = DefTokenEncoder
                   ):String = {
    val hash = enc(msgs)
    hash2Str(hash)
  }

}
