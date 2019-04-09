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

  def str2Hash(str:String):Array[Byte] = {
    if (str.length % 2 != 0)
      throw new IllegalArgumentException(s"Expecting even length")
    else {
      val len = str.length / 2
      val res = new Array[Byte](len)
      (0 until len).foreach { idx =>
        val hex = str.substring(idx*2, idx*2+2)
        val i = Integer.parseInt(hex, 16)
        res(idx) = (i & 0xff).asInstanceOf[Byte]
      }
      res
    }
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

  def generateTokenJ(msgs:Array[String]):String = {
    generateToken(msgs)
  }

}
