package org.xg.dbModels

import org.xg.json.CommonUtils

case class IJStr(cc:String, v:String)

case class CCStrMap(private val _m:Map[String, String]) {
  def by(cc:String):Option[String] = _m.get(cc)
  def byJ(cc:String):String = by(cc).orNull
}

object IJStr {
  def fromJsons(j:String):Array[IJStr] = CommonUtils._fromJsons(j)

  def toJsons(ijStrs:Array[IJStr]):String = CommonUtils._toJsons(ijStrs)

  def mapFromJsons(j:String):CCStrMap =
    CCStrMap(fromJsons(j).map(p => p.cc -> p.v).toMap)

}