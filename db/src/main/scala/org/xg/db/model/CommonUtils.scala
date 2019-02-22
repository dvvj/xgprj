package org.xg.db.model

import org.json4s.DefaultFormats

object CommonUtils {
  def _toJsons[T : Manifest](customers:Array[T]):String = {
    import org.json4s.jackson.Serialization._
    write(customers)(DefaultFormats)
  }

  def _fromJsons[T : Manifest](j:String):Array[T] = {
    import org.json4s.jackson.JsonMethods._
    implicit val fmt = DefaultFormats
    parse(j).extract[Array[T]]
  }
}
