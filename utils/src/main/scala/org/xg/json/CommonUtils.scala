package org.xg.json

import org.json4s.DefaultFormats

object CommonUtils {
  import org.json4s.jackson.Serialization._
  import org.json4s.jackson.JsonMethods._
  def _toJsons[T : Manifest](objs:Array[T]):String = {
    write(objs)(DefaultFormats)
  }

  def _fromJsons[T : Manifest](j:String):Array[T] = {
    implicit val fmt = DefaultFormats
    parse(j).extract[Array[T]]
  }

  def _toJson[T <: AnyRef](obj:T):String = {
    write(obj)(DefaultFormats)
  }

  def _fromJson[T : Manifest](j:String):T = {
    implicit val _fmt = DefaultFormats
//    println("_fromJson: " + j)
    parse(j).extract[T]
  }

}
